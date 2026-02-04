public class BankAccount {

    private String accountHolder;
    private  double accountBalance;


    public BankAccount(String accountHolder, double initialBalance){
        this.accountHolder = accountHolder;
        if (initialBalance > 0){
            this.accountBalance = initialBalance;
        }
    }

    //getters and setters


    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double newAccountBalance) {
        if(newAccountBalance >= 0){
            this.accountBalance = newAccountBalance;
        }else {
            System.err.println("Error: Balance cannot be negative.");
        }

    }
}
