package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       TransactionProcessor transactionProcessor = new TransactionProcessor();

       //Test Case 1: Valid Transaction
        Transaction txn1 = new Transaction("1234567890",5000.00,"USD","DEBIT");
        transactionProcessor.processTransaction(txn1);

        //Test Case 2: Invalid Transaction with amount too high
        Transaction txn2 = new Transaction("1234567890",15000.00,"USD","DEBIT");
        transactionProcessor.processTransaction(txn2);


        Transaction txn3 = new Transaction("1234567892",1000.00,"EUR","DEBIT");
        transactionProcessor.processTransaction(txn3);
    }
}
