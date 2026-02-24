package com.securebank.controller;

import com.securebank.model.Customer;
import com.securebank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
//WEB LAYER - handles HTTP requests. Delegates @Service
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //Simulates POST method /customers/register
    public String registerCustomer(String name, String email) {
        System.out.println("\n [HTTP] POST /customers/register");

        try{
            Customer customer = customerService.registerCustomer(name, email);
            return "201 created: "+customer;
        }catch (IllegalArgumentException e){
            return "400 Bad Request "+e.getMessage();
        }
    }

    //Simulates: GET /customers/customer/{id}
    public String getCustomer(String id) {
        System.out.println("\n [HTTP] GET /customers/customer/{id}");

        try {
            return "200 OK" +customerService.findCustomerById(id);
        }catch (IllegalArgumentException e){
            return "404 Not Found: "+e.getMessage();
        }
    }

}
