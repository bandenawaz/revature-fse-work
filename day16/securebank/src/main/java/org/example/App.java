package org.example;

import org.example.config.BankConfig;
import org.example.service.AccountService;
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

        System.out.println("\n***********  Container Ready  ***********");

        //Get Bean Notification Service is injected by Spring
        AccountService accountService = (AccountService) context.getBean(AccountService.class);
        accountService.processDeposit("Alice", 5000.0);

       ( (AnnotationConfigApplicationContext) (context)).close();



    }
}
