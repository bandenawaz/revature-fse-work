package com.securebank;

import com.securebank.config.BankConfig;
import com.securebank.service.AccountService;
import com.securebank.service.PaymentProcessor;
import com.securebank.service.ReportService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "***********  SECURE BANK  ***********" );

        //Spring container starts - reads BankConfig, creates all beans
        ApplicationContext context = new AnnotationConfigApplicationContext(BankConfig.class);

//        System.out.println("\n***********  Container Ready  ***********");
//
//        //Get Bean Notification Service is injected by Spring
//        AccountService accountService = (AccountService) context.getBean(AccountService.class);
//        accountService.processDeposit("Alice", 5000.0);
//
//        System.out.println("\nSpring created everything. You did nothing \n");
//        ReportService reportService = context.getBean(ReportService.class);
//        reportService.generateReport();
//
//        // PROOF: Lets list all the beans created by itself
//        System.out.println("\n********* BEANS SPRING CREATED ***********\n");
//        for(String name : context.getBeanDefinitionNames()){
//
//                System.out.println(" Bean: " + name);
//        }
//
//        // SINGLETON PROOF: same oject everytime
//        ReportService reportService1 = context.getBean(ReportService.class);
//        ReportService reportService2 = context.getBean(ReportService.class);
//        System.out.println("\n Same ReportService instance ? "+(reportService1 == reportService2));

        PaymentProcessor paymentProcessor = context.getBean(PaymentProcessor.class);
        paymentProcessor.processTransfer("ACC-001","ACC-002",5000.00);
        paymentProcessor.processTransfer("ACC-003","ACC-001",15000.00);
       ( (AnnotationConfigApplicationContext) (context)).close();



    }



}
