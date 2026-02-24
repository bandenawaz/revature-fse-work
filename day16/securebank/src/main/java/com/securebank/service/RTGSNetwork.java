package com.securebank.service;

import com.securebank.interfaces.PaymentNetwork;

public class RTGSNetwork implements PaymentNetwork {

    private String bankCode;

    public RTGSNetwork(String bankCode) {
        this.bankCode = bankCode;
        System.out.println("RTGS Network is ready. Code is :"+bankCode);
    }

    @Override
    public boolean transferFunds(String from, String to, String amount) {
        System.out.println("[RTGS-"+bankCode+"] Transfer Funds from "+from+" to "+to+" | $"+amount);
        return true;
    }

    @Override
    public String getNetworkName() {
        return "RTGS Domestic ( "+bankCode+")";
    }
}
