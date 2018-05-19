package org.odie.imissodie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloWorld {

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

}
