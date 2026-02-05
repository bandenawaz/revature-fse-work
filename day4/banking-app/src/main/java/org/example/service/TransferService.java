package org.example.service;

import org.example.exception.InsufficientBalanceException;
import org.example.exception.InternalSystemException;

import java.io.FileWriter;
import java.io.IOException;

public class TransferService {

    private double balance = 100000.00; //Mock db value

    /*
    Method: transferAmount()
    @params: amount(double)
    To transfer the amount to certain account
     */
    public void transfer(double amount) throws InternalSystemException, InsufficientBalanceException {
        //Business logic validation
        if(amount > balance){
            throw new InsufficientBalanceException("Transfer Failed: Insufficient funds. " +
                    "Current Balance: "+ balance);
        }

        //2. perform transfer
        balance -= amount;

        //3. Risky Operation(Logging to Audit file)
        try(FileWriter writer = new FileWriter("auditfile.txt", true)){
            writer.write("Transferred: $"+amount + ".\n");
        }catch (IOException exception){
            //lets wrap technical exception for a meaningful message
            throw new InternalSystemException("Failed to update audit logs." +
                    "Transaction rolled bakc", exception);
        }

    }
}
