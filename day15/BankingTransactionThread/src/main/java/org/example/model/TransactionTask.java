package org.example.model;

public class TransactionTask implements Runnable{

    private BankAccount bankAccount;
    private String transactionType;   //WITHDRAW or "DEPOSIT
    private double amount;
    private String customerName;

    //Constructor
    public TransactionTask(BankAccount bankAccount,
                           String transactionType, double amount, String customerName) {
        this.bankAccount = bankAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.customerName = customerName;
    }

    @Override
    public void run() {

        System.out.println("\n Transaction started by "+Thread.currentThread().getName()+"!");
        switch (transactionType) {
            case "DEPOSIT":
                bankAccount.deposit(amount,customerName);
                break;

                case "WITHDRAW":
                    bankAccount.withdraw(amount,customerName);
                    break;

        }

        //After successfull transaction, send notification
        //in real system, this would trigger an SMS/Email service
        System.out.println("[ "+Thread.currentThread().getName()+
                "] Notification sent to " + customerName);

    }
}
