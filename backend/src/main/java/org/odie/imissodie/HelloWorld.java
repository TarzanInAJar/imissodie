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
@RequestMapping("/")
public class HelloWorld {

    private Clip soundClip;

    @PostConstruct
    public void init(@Value("${soundfile}") File soundFile, @Value("${desiredMixer}") String desiredMixer) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (soundFile != null && soundFile.exists()){
            soundClip = getClip(getSpeaker(desiredMixer), soundFile);
        }
    }

    @GetMapping("/call")
    public ResponseEntity<Void> call() {
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
        try(AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile)){
            Clip clip = AudioSystem.getClip(speaker);
            clip.open(inputStream);
            return clip;
        }
    }

    private Mixer.Info getSpeaker(String deviceName){
        return stream(AudioSystem.getMixerInfo())
                .filter(mixer -> mixer.getName().equals(deviceName))
                .findFirst()
                .orElse(null);
    }

}
