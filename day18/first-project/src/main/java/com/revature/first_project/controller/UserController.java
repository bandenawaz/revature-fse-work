package com.revature.first_project.controller;

import com.revature.first_project.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
public class UserController {

    private User user;

    @GetMapping("/users")
    public String getUsers() {
        log.info("Fetching all users");
        log.debug("Debug information here");
        log.error("Something went wrong");
        return "User COmplete Details";
    }
}
