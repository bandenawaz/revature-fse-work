package org.example.controller;

import org.example.exception.InsufficientBalanceException;
import org.example.exception.InternalSystemException;
import org.example.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService = new TransferService();


    public void processTransferRequest(String user, double amount){

        //Setup MDC for traceability
        String txId = UUID.randomUUID().toString().substring(0,8);
        MDC.put("txId", txId);
        MDC.put("user", user);
        MDC.put("amount", String.valueOf(amount));

        try{
            logger.info("Initiating transfer request for user {}, amount {} ", user, amount);
            transferService.transfer(user, "EXTERNAL_ACCOUNT",amount);
            logger.info("SUCCESS: Transfer of $ "+amount +"completed");
        }catch (InsufficientBalanceException e){
            //Log as warning - this is user error
            logger.warn("Transfer of $ "+amount +"denied");
        }catch (InternalSystemException ie) {
            logger.error("System failure during request. Reference ID: {}", txId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred. ",e);

        }finally {
            System.out.println("SESSION : Transfer request processed");
        }
    }
}
