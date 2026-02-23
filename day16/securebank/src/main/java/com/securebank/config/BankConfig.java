package com.securebank.config;

import com.securebank.db.DBConnection;
import com.securebank.service.AccountService;
import com.securebank.service.NotificationService;
import com.securebank.service.ReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankConfig {


    //Lets create Object
    @Bean
    public NotificationService notificationService(){
        return new NotificationService("smtp.securebank.com");
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
