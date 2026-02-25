package com.revature.first_project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class FirstController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }
}
