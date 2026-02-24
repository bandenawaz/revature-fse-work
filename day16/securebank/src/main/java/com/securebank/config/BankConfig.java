package com.securebank.config;

import com.securebank.db.DBConnection;
import com.securebank.interfaces.PaymentNetwork;
import com.securebank.service.*;
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

    @Bean
    public PaymentNetwork paymentNetwork(){
        return new RTGSNetwork("SECB0001");
    }

    @Bean
    public PaymentProcessor paymentProcessor(){
        return new PaymentProcessor(paymentNetwork());
    }

    @Bean
    public CreditScoringSystem creditScoringSystem(){
        return new CreditScoringSystem();
    }

    @Bean
    public InsuranceService insuranceService(){
        return new InsuranceService("TrustShield insurance");
    }

    @Bean
    public LoanService loanService(){
        //TYPE 1: constuctor inject - mandatory dep
        LoanService loanService = new LoanService(creditScoringSystem(), 500_000.00);

        //TYPE 2: setter injection - optional dep
        loanService.setInsuranceService(insuranceService());
        return loanService;
    }
}
