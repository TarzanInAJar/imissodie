package org.odie.imissodie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.util.Arrays.stream;

@Controller
@RequestMapping("/hello")
public class HelloWorld {

    @Value("${soundfile}")
    File soundfile;

    @Value("${desiredMixer}")
    String desiredMixer;

    private Clip soundClip;


    @PostConstruct
    public void init() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (soundfile != null && soundfile.exists()){
            soundClip = getClip(getSpeaker(desiredMixer), soundfile);
        }
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping("/horn")
    public ResponseEntity<Void> playHorn() {
        playSound(soundClip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void playSound(Clip clip) {
        if (clip.isRunning()){
            clip.stop();
            clip.setFramePosition(0);
        }
        clip.start();
    }

    private Clip getClip(Mixer.Info speaker, File soundfile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile);
        Clip clip = AudioSystem.getClip(speaker);
        clip.open(inputStream);
        inputStream.close();
        return clip;
    }

    private Mixer.Info getSpeaker(String deviceName){
        return stream(AudioSystem.getMixerInfo())
                .filter(mixer -> mixer.getName().equals(deviceName))
                .findFirst()
                .orElse(null);
    }

}
