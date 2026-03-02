package org.revature.springsecurityapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/public/hello")
    public String publicHello(){
        return "Hello! This is a PUBLIC endpoint. No login needed";
    }
    @GetMapping("/private/hello")
    public String privateHello(){
        return "Hello! You are authenticated. This is a PRIVATE endpoint.";

    }

    @GetMapping("/admin/hello")
    public String adminHello(){
        return "Hello Admin! This is a Admin-only endpoint.";
    }

    // Get(/manager/dashboard
    //Create a third inmemoryrole with MANAGER role can access it
    //Secure /manager/** so only users with Manager role cab access it


}
