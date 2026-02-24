package com.securebank;

import com.securebank.config.BankConfig;
import com.securebank.service.LoanService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DITypesDemo {

    public static void main(String[] args) {
        System.out.println(" DI Types Demo ");

        ApplicationContext context = new AnnotationConfigApplicationContext(BankConfig.class);

        LoanService loanService = (LoanService) context.getBean(LoanService.class);


        loanService.applyForLoan("CUST-001", 25000.00);
        loanService.applyForLoan("CUST-002", 75000.00);
        loanService.applyForLoan("CUST-003", 100000.00);
        loanService.applyForLoan("CUST-004", 1000000.00);


        ((AnnotationConfigApplicationContext)context).close();
    }
}
