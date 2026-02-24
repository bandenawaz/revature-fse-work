package com.securebank.service;

import com.securebank.model.Customer;
import com.securebank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//BUSINESS LAYER - contains business rules and orchestration
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(String name, String email) {

        System.out.println("\n [Service] Registering Customer " + name + " with email " + email + "\n");

        /*
        Business rule 1: No duplicate emails
         */
        if (customerRepository.emailExists(email)) {
            throw new IllegalArgumentException("Customer with email " + email + " already exists");
        }

         /*
        Business rule 2: Save to DB
         */
        Customer customer = new Customer(customerRepository.nextId(), name, email);
        Customer savedCustomer = customerRepository.save(customer);

        System.out.println("[Service] Saved Customer " + savedCustomer + " with email " + email + "\n");
        return savedCustomer;
    }

    //get the customer by id
    public Customer findCustomerById(String id) {
        System.out.println("\n [Service] Finding Customer with id " + id + "\n");
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not dound: "+id));
    }
}
