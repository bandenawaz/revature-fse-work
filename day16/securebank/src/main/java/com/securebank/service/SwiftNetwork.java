package com.securebank.service;

import com.securebank.interfaces.PaymentNetwork;

public class SwiftNetwork implements PaymentNetwork {

    private String swiftCode;

    public SwiftNetwork(String swiftCode) {
        this.swiftCode = swiftCode;
        System.out.println("Swift Network is ready. Code is :"+swiftCode);
    }
    @Override
    public boolean transferFunds(String from, String to, String amount) {
        System.out.println("[SWIFT-"+swiftCode+"] Transfer Funds from "+from+" to "+to+" | $"+amount);

        return false;
    }

    @Override
    public String getNetworkName() {
        return "SWIFT Internation ( \"+swiftCode\")";
    }
}
