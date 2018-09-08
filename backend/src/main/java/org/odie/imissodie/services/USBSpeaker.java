package org.odie.imissodie.services;

import ilarkesto.media.Audio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.util.Arrays.stream;

@Service
public class USBSpeaker {

    @Value("${soundfile}")
    File soundFile;
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

        audioDevice = getAudioDevice(desiredAudioDevice);
        audioMixer = getAudioMixer(desiredAudioDevice);

        if (soundFile != null && soundFile.exists()){

            soundClip = getClip(soundFile);
            setVolume(defaultVolume);
        }
    }

    public synchronized void playSound(Optional<Float> volume) throws InterruptedException, LineUnavailableException {
        CountDownLatch syncLatch = new CountDownLatch(1);

        if (volume.isPresent() && !volume.get().equals(defaultVolume)){
            setVolume(volume.get());
        }

        soundClip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                syncLatch.countDown();
            }
        });

        soundClip.start();
        syncLatch.await();

        if (volume.isPresent() && !volume.get().equals(defaultVolume)){
            setVolume(defaultVolume);
        }
        soundClip.setFramePosition(0);
    }

    private void setVolume(Float val) {

        Audio.getAvailableOutputLines(audioMixer).stream().forEach(line -> {
            log.debug(String.format("Searching line '%s' for volume controls", line.getLineInfo().toString()));

            boolean opened = Audio.open(line);
            Arrays.stream(line.getControls())
                    .flatMap(control -> control instanceof CompoundControl? Stream.of(((CompoundControl) control).getMemberControls()): Stream.of(control))
                    .filter(control -> control instanceof FloatControl)
                    .filter(control -> control.getType() == FloatControl.Type.VOLUME)
                    .forEach(control -> ((FloatControl) control).setValue(val));

            if(opened) line.close();
        });

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

    private Clip getClip(File soundfile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        try(AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile)){
            Clip clip = AudioSystem.getClip(audioDevice.getMixerInfo());
            clip.open(inputStream);
            return clip;
        }
    }
}
