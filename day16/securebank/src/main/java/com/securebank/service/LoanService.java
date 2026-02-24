package com.securebank.service;

public class LoanService {

    // 1. CONSTRUCTOR INJECTION
    private final CreditScoringSystem creditScoringSystem;
    private final double maxLoanAmount;


    public LoanService(CreditScoringSystem creditScoringSystem, double maxLoanAmount) {
        this.creditScoringSystem = creditScoringSystem;
        this.maxLoanAmount = maxLoanAmount;

        System.out.println("LoanService created. Max loan amount is:  $" + maxLoanAmount);
    }
    //2. Setter Injection
    private InsuranceService insuranceService;

    public void setInsuranceService(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
        System.out.println(" Insurance Service is configured: " +(insuranceService != null));
    }

    //Business logic use both
    public void applyForLoan(String customerId, double amount){

        System.out.println("\n ---------- LOAN Application : "+customerId+ "for $"+amount+" -----------");
        if (amount > maxLoanAmount) {
            System.out.println("REJECTED: Exceeds max loan of $"+maxLoanAmount);
            return;
        }

        int score = creditScoringSystem.calculateScore(customerId);
        System.out.println(" Credit Score: "+score);

        if(!creditScoringSystem.isEligible(score)){
            System.out.println("REJECTED: Credit Score too low");
            return;
        }

        System.out.println(" Credit check passed ");

        //Optional dep - works with or without insurance
        if (insuranceService != null){
            double premium = insuranceService.calculatePremium(amount);
            System.out.println(" Insurance added: $"+premium);
        }else {
            System.out.println("REJECTED: Insurance service not available at this branch");
        }

        System.out.println(" LOAN APPROVED: $"+amount);
    }


}
