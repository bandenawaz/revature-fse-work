package org.revature.springsecurityapp.controller;

import lombok.RequiredArgsConstructor;
import org.revature.springsecurityapp.entity.User;
import org.revature.springsecurityapp.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;


    //Only Admins can disable account
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/disable/{username}")
    public String disableUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);
        return "User "+username+" has been disabled";
    }
}
