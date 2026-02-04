public class ATMSystem {

    public static void main(String[] args) {

        double myBalance = 5000.00;
        double withdrawalRequest = 8000.00;

        //lets call the method
        if(canWithdraw(myBalance, withdrawalRequest)){
            myBalance -= withdrawalRequest;
            printReceipt("Abhishek", myBalance);
        }else {
            System.err.println("Error: Insufficient funds");
            System.out.println("Available Balance: " + myBalance);
        }

    }

    //1. A method with params and a return value
    //method where i can withdraw
    public static boolean canWithdraw(double balance, double amount){
        System.out.println("Checking: Is ₹" +amount+ " available?");

        return balance >= amount;
    }
    //2. A void method to print receipt
    public static void printReceipt(String name, double remainingBalance){
        System.out.println("----------------------------------");
        System.out.println("Customer: "+name);
        System.out.println("Remaining Balance: "+remainingBalance);
        System.out.println("----------------------------------");

    }
}

