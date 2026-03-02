package org.revature.springsecuritydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello! This is a PUBLIC endpoint. No login needed.";
    }

    @GetMapping("/private/hello")
    public String privateHello() {
        return "Hello! You are authenticated. This is a PRIVATE endpoint.";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hello Admin! This is an ADMIN-only endpoint.";
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "Hello Manager! This is an Manager Dashboard endpoint.";
    }
}