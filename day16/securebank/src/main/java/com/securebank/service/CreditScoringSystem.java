package com.securebank.service;

public class CreditScoringSystem {

    public int calculateScore(String customerId){
        System.out.println(" -> Calculating score for "+customerId);
        return 750;
    }

    public boolean isEligible(int score){
        return score >= 650;
    }
}
