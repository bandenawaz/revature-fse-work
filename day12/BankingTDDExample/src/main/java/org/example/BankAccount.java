package org.example;

public class BankAccount {
    private double balance; //java initializes double to 0.0
    private String accountType;
    private  int transactionCount;

    //Constructor
    public BankAccount(String accountType) {
        this.accountType = accountType;
        this.balance = 0.0;
        this.transactionCount = 0;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than 0."
            );
        }
        balance += amount;
        transactionCount++;
    }


public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than 0.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        balance -= amount;
        transactionCount++;

    }

    /*
    * Calculates the Service charges for this account
    * Business Rule:
    * - SAVINGS: no charge if balance >= 1000, else charge $5.
    * - CURRENT: flat $10 monthly charge
    * - If transaction count > 10, add $2 extra charges for excessive transactions
     */
    public double calculateServiceCharge() {
        double charge = 0.0;

        // BRANCH 1: Account type check
        if ("SAVINGS".equals(accountType)) {
            //BRANCH 2: Balance check for the savings
            if (balance >= 1000){

                charge = 0.0;  //Free for maintaining minimum balance

            }else {

                charge = 5.0; // Fee for low balance

            }
        }else {
            //CURRENT account
            charge = 10.0;
        }

        //BRANCH 3: Excessive Transaction
        if (transactionCount > 10){
            charge += 2.0;
        }
        return charge;
    }
}
