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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping("/horn")
    public ResponseEntity<Void> playHorn() {
        try {
            playSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.error("Exception occurred playing soundfile: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void playSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Mixer.Info speaker = getSpeaker(this.desiredMixer);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile);
        Clip clip = AudioSystem.getClip(speaker);
        clip.open(inputStream);
        clip.start();
        inputStream.close();
    }

    private Mixer.Info getSpeaker(String deviceName){
        return stream(AudioSystem.getMixerInfo())
                .filter(mixer -> mixer.getName().equals(deviceName))
                .findFirst()
                .orElse(null);
    }

}
