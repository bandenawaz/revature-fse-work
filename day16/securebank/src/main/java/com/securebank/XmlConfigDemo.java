package com.securebank;

import com.securebank.service.TransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlConfigDemo {
    public static void main(String[] args) {
        System.out.println(" ********* XML Configuration Demo *********");

        // lets read the application context file
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("\n ======= ALL XML Beans are Ready =======");

        TransactionService transactionService = (TransactionService) context.getBean("transactionService");

        transactionService.execute("ACC-101", "ACC-202",5000.00,"TRANSFER");
        transactionService.execute("ACC-303", "ACC-101",250000.00,"WIRE");

        ((ClassPathXmlApplicationContext)context).close();
    }
}
