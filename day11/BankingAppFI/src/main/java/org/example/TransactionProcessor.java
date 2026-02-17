package org.example;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TransactionProcessor {

    // 1. PREDICATE: Validation rules
    // Business Rule: Amount must be positive and less than $10,000
    Predicate<Transaction> isValidAmount = (txn) ->
            txn.amount > 0 && txn.amount < 10000;

    //Business Rule: Account number must be 10 digits
    Predicate<Transaction> isValidAccountNumber = (txn) ->
            txn.accountNumber != null && txn.accountNumber.length() == 10;

    //Lets combine multiple predicates
    Predicate<Transaction> isValidTransaction = isValidAmount.and(isValidAccountNumber);

    //Step 2: FUNCTION - Data Transformation
    //Convert USD to Rupees(simplified 1 USD = 98Rs)
    Function<Double, Double> usdToInr = (usd) -> usd * 98.00;

    //Convert USD to EUR
    Function<Double, Double> usdToEur = usd -> usd * .85;

    //Convert USD to GBP
    Function<Double, Double> usdToGbp = usd -> usd * 0.73;

    //Apply transaction fee (2% deduction)
    Function<Double, Double> applyFee = (amount) -> amount * 0.98;


    //CONSUMER: Actions(Logging, Notifications)
    //Log transactions to console(production: log to file/database)
    Consumer<Transaction> logTransaction = (txn) ->
            System.out.println("[LOG] "+ LocalDateTime.now()+ " - "+txn);


    //Send Notifications(Simulated)
    Consumer<Transaction> sendNotification = (txn) ->
            System.out.println("[Notification] Transaction Alert sent for "+ txn.accountNumber);

    //Combine multiple Consumers(execute Both)
    Consumer<Transaction> auditTrail =
            logTransaction.andThen(sendNotification);

    //SUPPLIER: Generate Values
    //Generate Unique Transaction Id
    Supplier<String> generateTransactionId = () ->
            "TXN-"+ UUID.randomUUID().toString().substring(0,8).toUpperCase();

    //GET Current Timestamp
    Supplier<String> getCurrentTimeStamp = () ->
            LocalDateTime.now().toString();

    // MAIN PROCESSING METHOD
    public void processTransaction(Transaction transaction) {
        System.out.println("\n ========== PROCESSING TRANSACTION ========== ");

        //Step1: Generate Transaction ID
        String transactionId = generateTransactionId.get();
        System.out.println("Transaction ID: " + transactionId);

        //Step 2: Validate
        if (!isValidTransaction.test(transaction)) {
            System.out.println("REJECTED: Invalid Transaction");
            return;
        }
        System.out.println("VALIDATED: Transaction is Valid");

        //Step 3: Transform(apply fee)
        double finalAmount = applyFee.apply(transaction.amount);
        System.out.println("Amount after fee: $"+ String.format("%.2f", finalAmount));


        //Step 4: Currency Conversion(if needed)
        if (transaction.currency.equals("EUR")){
            double eurAmount = usdToEur.apply(finalAmount);
            System.out.println("Amount after EUR: £"+ String.format("%.2f", eurAmount));
        }

        //Step 5: Log and Notify
        auditTrail.accept(transaction);


        System.out.println("Transaction Alert sent for "+ transaction.accountNumber);
        System.out.println("Transaction COMPETED SUCCESSFULLY");
        System.out.println("================================== ");
    }

}
