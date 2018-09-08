package org.odie.imissodie.controllers;

import ilarkesto.media.Audio;
import org.odie.imissodie.services.USBSpeaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.util.Arrays.stream;

@Controller
@RequestMapping("/")
public class OdieController {

    @Autowired
    USBSpeaker speaker;

    private final Logger log = LoggerFactory.getLogger(this.getClass());





    @GetMapping("/call")
    public ResponseEntity<Object> call(@RequestParam("volume") Optional<Float> volume) {

        try {
            speaker.playSound(volume);
        } catch (InterruptedException | LineUnavailableException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }









}
