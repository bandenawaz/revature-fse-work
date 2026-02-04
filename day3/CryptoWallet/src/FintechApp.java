public class FintechApp {
    public static void main(String[] args) {

        Payable myAccount = new CryptoWallet("Karthik",
                10000.00,")xABC123");

        myAccount.processTransaction(200.0);
        System.out.println("Current Platofrm Fee is: "+Wallet.class.getName());



        //GARBAGE Collection
        //Karthik decides to close his account
        myAccount = null;
        System.out.println("Account closed. Memory will be reclaimed by GC");


    }
}
