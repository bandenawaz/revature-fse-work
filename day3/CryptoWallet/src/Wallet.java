abstract class Wallet implements Payable{
    private static double platformFee = 0.05;

    private String ownerName;
    private double balance;

    public Wallet(String ownerName, double initialDeposit){
        this.ownerName = ownerName;
        this.balance = initialDeposit;
    }

    public double getBalance(){
        return this.balance;
    }

    protected void updateBalance(double amount){
        if(this.balance + amount >= 0){
            this.balance += amount;
        }else {
            System.err.println("Insufficient funds for "+ownerName);
        }
    }

    //abstract method
    public abstract void showWalletType();
}
