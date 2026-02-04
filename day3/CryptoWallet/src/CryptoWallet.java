public class CryptoWallet extends Wallet{

    private String walletAddress;
    public CryptoWallet(String owner, double deposit, String walletAddress){
        super(owner,deposit); //Calling the Parent Constructor
        this.walletAddress = walletAddress;

    }

    @Override
    public void showWalletType() {
        System.out.println("This is a secure Crypto Wallet at: "+walletAddress);
    }

    @Override
    public boolean processTransaction(double amount) {
        System.out.println("Verifying blockchain transaction.....");
        updateBalance(-amount);
        return true;
    }
}
