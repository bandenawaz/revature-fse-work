package com.securebank.service;

public class InsuranceService {

    private String provider;

    public InsuranceService(String provider) {
        this.provider = provider;
    }

    public  double calculatePremium(double loanAmount){

        double premium = loanAmount * 0.005;
        System.out.println(" -> Insurance ("+ provider +" ): $"+premium);
        return premium;
    }
}
