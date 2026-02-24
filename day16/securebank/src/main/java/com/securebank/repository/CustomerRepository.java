package com.securebank.repository;

import com.securebank.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
//DATA LAYER - talks to DB. Gets exception translation as bonus
public class CustomerRepository {

    private Map<String, Customer> customers =  new HashMap<>();
    private int idCounter = 100;

    public CustomerRepository() {
        System.out.println("Customer Repository created [DATA LAYER]");

        //Pre-Load some data
        customers.put("C100", new Customer("C100",
                "Alice Johnson", "alice@securebank.com"));
        customers.put("C101", new Customer("C101",
                "Bob Smith", "bob@securebank.com"));
    }

    //lets save customer to the database
    public Customer save(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        System.out.println("Saving customer [" + customer.getCustomerId() + "]");
        return customer;
    }

    public Optional<Customer> findById(String id) {
        System.out.println(" [DB] Select WHERE id = '"+id+"'");
        return Optional.ofNullable(customers.get(id));
    }

    public boolean emailExists(String email) {
       return customers.values().stream()
               .anyMatch(customer ->
                       customer.getCustomerEmail().equalsIgnoreCase(email));
    }

    public String nextId() {
       return "C" + ++idCounter;
    }
}
