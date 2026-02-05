package org.example.controller;

import org.example.exception.InsufficientBalanceException;
import org.example.exception.InternalSystemException;
import org.example.service.TransferService;

public class TransferController {

    private final TransferService transferService = new TransferService();


    public void postTransger(double amount){
        try{
            transferService.transfer(amount);
            System.out.println("SUCCESS: Transfer of $ "+amount +"completed");
        }catch (InsufficientBalanceException e){
            //Log as warning - this is user error
            System.out.println("USER NOTICE: "+e.getMessage());
        }catch (InternalSystemException ie){
            System.err.println("CRITICAL : "+ ie.getMessage());
            System.err.println("REASON :"+ ie.getCause().getMessage());
        }finally {
            System.out.println("SESSION : Transfer request processed");
        }
    }
}
