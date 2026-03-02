package org.revature.springsecurityapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    /*
    A public-looking endpoint - but is it really public?
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello! You are authenticated";
    }
}
