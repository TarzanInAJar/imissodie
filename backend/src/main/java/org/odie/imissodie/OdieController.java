package org.odie.imissodie;

import ilarkesto.media.Audio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;

@Controller
@RequestMapping("/")
public class OdieController {

    @Value("${soundfile}") File soundFile;
    @Value("${desiredAudioDevice}") String desiredAudioDevice;
    @Value("${defaultVolume:0.5}") Float defaultVolume;

    private Mixer audioDevice;
    private Mixer audioMixer;
    private Clip soundClip;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try {
            log.debug(Audio.getHierarchyInfo());
        } catch (Exception e){
            log.warn("Couldn't retrieve audio information for debug logs! " + e.getMessage());
        }

        getMixers(desiredAudioDevice);

        if (soundFile != null && soundFile.exists()){

            soundClip = getClip(soundFile);
            setVolume(defaultVolume);
        }
    }

    private void getMixers(String desiredAudioDevice) {
        audioDevice = getAudioDevice(desiredAudioDevice);
        audioMixer = getAudioMixer(desiredAudioDevice);
    }

    @GetMapping("/call")
    public ResponseEntity<Object> call(@RequestParam("volume") Optional<Float> volume) {

        try {
            playSound(soundClip, volume);
        } catch (InterruptedException | LineUnavailableException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private synchronized void playSound(Clip clip, Optional<Float> volume) throws InterruptedException, LineUnavailableException {
        CountDownLatch syncLatch = new CountDownLatch(1);

        if (volume.isPresent() && !volume.get().equals(defaultVolume)){
            setVolume(volume.get());
        }

        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                syncLatch.countDown();
            }
        });

        clip.start();
        syncLatch.await();

        if (volume.isPresent() && !volume.get().equals(defaultVolume)){
            setVolume(defaultVolume);
        }
        clip.setFramePosition(0);
    }

    private Clip getClip(File soundfile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        try(AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile)){
            Clip clip = AudioSystem.getClip(audioDevice.getMixerInfo());
            clip.open(inputStream);
            return clip;
        }
    }

    private Mixer getAudioDevice(String deviceName){
        Mixer mixer = getMixer(deviceName, "Direct Audio Device");
        log.debug("Returning AudioDevice: " + mixer.getMixerInfo().getName() + ":" + mixer.getMixerInfo().getDescription());
        return mixer;
    }

    private Mixer getAudioMixer(String deviceName){
        Mixer mixer = getMixer(deviceName, "Mixer");
        log.debug("Returning AudioMixer: " + mixer.getMixerInfo().getName() + ":" + mixer.getMixerInfo().getDescription());
        return mixer;
    }

    private Mixer getMixer(String deviceName, String description){
        return AudioSystem.getMixer(
                stream(AudioSystem.getMixerInfo())
                        .filter(mixer -> mixer.getName().contains(deviceName))
                        .filter(mixer -> mixer.getDescription().contains(description))
                        .collect(onlyElement()));
    }

    private void setVolume(Float val){
        log.debug("Setting volume to: " + val);
        log.debug(Audio.getHierarchyInfo());
        Audio.getAvailableOutputLines(audioMixer).stream()
                .forEach(line -> setVolume(line, val));
    }

    private void setVolume(Line line, Float val){
        log.debug(String.format("Searching line '%s' for volume controls", line.getLineInfo().toString()));

        Audio.open(line);
        for (Control control : line.getControls()){
            log.debug("Processing control: " + control.toString());
            if (control instanceof FloatControl && control.getType() == FloatControl.Type.VOLUME) {
                log.debug("Setting volumeControl: " + control.toString());
                ((FloatControl) control).setValue(val);
            } else if (control instanceof CompoundControl) {
                for (Control subControl : ((CompoundControl) control).getMemberControls()){
                    log.debug("Processing subControl: " + subControl.toString());
                    if (subControl instanceof FloatControl && subControl.getType() == FloatControl.Type.VOLUME) {
                        log.debug("Setting subControl volumeControl " + subControl.toString());
                        ((FloatControl) subControl).setValue(val);
                    }
                }
            }
        }
        if (line.isOpen()){
            line.close();
        }
    }

}
