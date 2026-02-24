package com.securebank.service;

import com.securebank.interfaces.PaymentNetwork;

public class PaymentProcessor {

    private final PaymentNetwork paymentNetwork;
    private int count = 0;

    public PaymentProcessor(PaymentNetwork paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
        System.out.println("PaymentProcessor Ready. Network Name: " + paymentNetwork.getNetworkName());
    }

    public void processTransfer(String from , String to ,double amount) {
        count++;
        System.out.println("\n[Transaction #"+count+ " ]");
        boolean success = paymentNetwork.transferFunds(from , to, String.valueOf(amount));
        System.out.println(success ? "Transfer Successful" : "Transfer Failed");
    }
}
