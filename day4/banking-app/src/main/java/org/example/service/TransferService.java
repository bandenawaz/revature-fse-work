package org.example.service;

import org.example.exception.InsufficientBalanceException;
import org.example.exception.InternalSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private double balance = 1000.00; //Mock db value
    /*
    Method: transferAmount()
    @params: amount(double)
    To transfer the amount to certain account
     */
    public void transfer(String from, String to, double amount) throws InternalSystemException, InsufficientBalanceException {
        //Business logic validation
        if(amount > balance){
            logger.warn("Validation Failed: {} attempted to send ${} but only has ${}",from,to,amount);
            throw new InsufficientBalanceException("Transfer Failed: Insufficient funds. " +
                    "Current Balance: "+ balance);
        }

        //2. perform transfer
        balance -= amount;
        logger.info("Transfer from "+from+" to "+to+" to "+amount+" was successful");
        logger.debug("Local balance updated. New Balance is: "+balance);

        //3. Risky Operation(Logging to Audit file)
        try(FileWriter writer = new FileWriter("auditfile.txt", true)){
            writer.write("Transferred: $"+amount + ".\n");
        }catch (IOException exception){
            //lets wrap technical exception for a meaningful message
            logger.error("IO Failure during audit logging", exception);
            throw new InternalSystemException("Failed to update audit logs." +
                    "Transaction rolled back", exception);
        }

    }
}
