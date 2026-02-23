package org.example.config;

import org.example.db.DBConnection;
import org.example.service.AccountService;
import org.example.service.NotificationService;
import org.example.service.ReportService;
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

    //Spring Controls when this object is born, with waht value
    @Bean
    public DBConnection dbConnection(){
        return new DBConnection("db.securebank.com",5432);
    }

    //Spring controls when this is created, injecting databaseConnection
    @Bean
    public ReportService reportService(){
        return new ReportService(dbConnection());
    }
}
