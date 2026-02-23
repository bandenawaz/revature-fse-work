package org.example.config;

import org.example.service.AccountService;
import org.example.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankConfig {


    //Lets create Object
    @Bean
    public NotificationService notificationService(){
        return new NotificationService("smpt.securebank.com");
    }

    @Bean
    public AccountService accountService(){
        return new AccountService(notificationService());
    }
}
