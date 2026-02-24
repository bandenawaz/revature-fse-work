package com.securebank;

import com.securebank.config.AnnotationConfig;
import com.securebank.controller.CustomerController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StereoTypeDemo {

    public static void main(String[] args) {

        System.out.println("StereoType Annotations Demo");
        System.out.println("Architecture: @Controller -> @Service -> @Repository\n");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationConfig.class);
        CustomerController customerController = context.getBean(CustomerController.class);
        System.out.println(customerController.registerCustomer("Charlie Brown", "charlie@bank.com"));
        System.out.println(customerController.registerCustomer("Bob Marley", "bob@bank.com"));

        System.out.println(customerController.getCustomer("C100"));
        System.out.println(customerController.getCustomer("C999"));

        context.close();
    }
}
