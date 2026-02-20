package org.example;

import org.example.model.BankAccount;
import org.example.model.TransactionTask;

import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "    NATIONAL BANK TRANSACTION SYSTEM    " );

        //Shared Bank Account (critical shared resource)
        BankAccount sharedAccount = new BankAccount("ACC-001-JOINT",10000.0);

        System.out.println("Account: " + sharedAccount.getAccountNumber());
        System.out.println("Initial Balance: $" + sharedAccount.getBalance());

        //Create Multiple transaction tasks(from different customemrs/ATMS)
        TransactionTask task1 = new TransactionTask(sharedAccount,"WITHDRAW",3000.0,"Varshini");
        TransactionTask task2 = new TransactionTask(sharedAccount,"WITHDRAW",5000.0,"Gopi");
        TransactionTask task3 = new TransactionTask(sharedAccount,"DEPOSIT",2000.0,"Yamini");
        TransactionTask task4 = new TransactionTask(sharedAccount,"WITHDRAW",4000.0,"Nagendra");

        //Assign each task to a thread
        Thread mumbaiATM =  new Thread(task1, "ATM-Mumbai-Thread");
        Thread delhiATM =  new Thread(task2, "ATM-Delhi-Thread");
        Thread branchTeller =  new Thread(task3, "BranchTeller-Thread");
        Thread onlineThread =  new Thread(task4, "Online-Banking-Thread");

        //Start all threads
        mumbaiATM.start();
        delhiATM.start();
        branchTeller.start();
        onlineThread.start();

        //Wait for all transactions to complete
        mumbaiATM.join();
        delhiATM.join();
        onlineThread.join();
        branchTeller.join();

        //FINAL STATE
        System.out.println("All transactions Processed");
        System.out.println("Final Account Balance: $" + sharedAccount.getBalance());







    }
}
