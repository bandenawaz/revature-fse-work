package com.securebank.model;

import java.util.List;

public class TransactionLedger {

    private String ledgerName;
    private String currency;
    private double transactionLimit;
    private List<String> allowedbranches;

    public TransactionLedger(String ledgerName, String currency) {
        this.ledgerName = ledgerName;
        this.currency = currency;
        System.out.println(" TransactionLedger created: "+ ledgerName);
    }

    public void setTransactionLimit(double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public void setAllowedbranches(List<String> allowedbranches) {
        this.allowedbranches = allowedbranches;
    }

    public void recordTransaction(String from, String to, double amount) {
        System.out.println("\n [ "+ledgerName+ " ]"+currency+" "+amount+
                ": "+from+" -> "+to);

        if (amount > transactionLimit) {
            System.out.println(" WARNING: Transaction Limit Exceeds of "+currency+
                    " "+transactionLimit);
        }

        System.out.println(" Allowed Branches: "+allowedbranches);
    }
}
