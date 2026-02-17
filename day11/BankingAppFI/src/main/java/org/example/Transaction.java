package org.example;

public class Transaction {
    String accountNumber;
    String currency;
    double amount;
    String type;

    //constructor
    public Transaction(String accountNumber, double amount,String currency, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " of " +amount + " "+currency+ "from account number " + accountNumber;
    }
}
