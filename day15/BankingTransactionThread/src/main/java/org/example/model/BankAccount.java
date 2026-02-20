package org.example.model;

import java.util.concurrent.locks.ReentrantLock;

/*
domain Model - The Shared Resource
 */
public class BankAccount {

    private String accountNumber;
    private  double balance;
    private ReentrantLock lock = new ReentrantLock(); //Thread safe locking

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    /*
    Thread Safe Withdrawal:
    Where customer can withdraw amount seamlessly
     */
    public boolean withdraw(double amount, String customerName) {
        /*
        Lets acquire the lock, so that only one thread can be here
         */
        lock.lock();

        try{
            System.out.println("[ "+Thread.currentThread().getName() +" ]"
            +customerName+ " attempting withdrawal of $ "+amount);

            if (balance >= amount) {
                //Simulate Processing time
                Thread.sleep(500);
                balance -= amount;
                System.out.println("[ "+Thread.currentThread().getName() +" ] SUCCESS! $ "+amount+
            "withdrawn by " +customerName+ "| Remaining balance is $ "+balance);
                return true;
            }else {
                System.out.println("[ "+Thread.currentThread().getName() +
                        " ] FAILED! Insufficient balance for" +customerName);
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
           return false;
        }finally {
            lock.unlock();  //Always release lock in finally block
        }

    }

    /*
    Thread Safe Deposit
    Where customers can deposit amount seamlessly
     */
    public void deposit(double amount, String customerName) {
        lock.lock();

        try {
            Thread.sleep(300);  //Simulate Processing
            balance += amount;
            System.out.println("[ "+Thread.currentThread().getName() +" ]" +
                    customerName+ " deposited $ "+amount +
                    "| New Balance is $ "+balance);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }finally {
            lock.unlock();
        }
    }

    //retrieve balance
    public double getBalance() { return balance; }

    public String getAccountNumber() { return accountNumber; }
}
