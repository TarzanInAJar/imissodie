package org.odie.imissodie;

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

@Controller
@RequestMapping("/hello")
public class HelloWorld {

    @Value("${soundfile}")
    File soundfile;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping("/horn")
    public ResponseEntity<Void> playHorn() {
        playSound();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void playSound() {
        try {

            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            //String desiredMixer = "Port Device [hw:1]";
            //String desiredMixer = "Port Device_1 [hw:2]";
            String desiredMixer = "Device_1 [plughw:2,0]";

            Mixer.Info speaker = null;
            for(int i = 0; i < mixerInfo.length; i++) {
                Mixer.Info info = mixerInfo[i];

                System.out.println(String.format("Name [%s] \n Description [%s]\n\n", info.getName(), info.getDescription()));

                if (info.getName().equals(desiredMixer)){
                    speaker = info;
                    break;
                }
            }

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile);
            Clip clip = AudioSystem.getClip(speaker);
            clip.open(inputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
