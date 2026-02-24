package com.securebank.interfaces;

public interface PaymentNetwork {
    boolean transferFunds(String from , String to , String amount);
    String getNetworkName();
}
