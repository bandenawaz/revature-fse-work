package com.securebank.model;

public class Customer {
    String customerId;
    String customerName;
    String customerEmail;

    public Customer(String customerId, String customerName, String customerEmail) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    @Override
    public String toString() {
        return "Customer[" +
                "customerId='" + customerId + " | "+
                "customerName='" + customerName + " | " +
                "customerEmail='" + customerEmail  +
                ']';
    }
}
