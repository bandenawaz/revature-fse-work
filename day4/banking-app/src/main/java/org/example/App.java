package org.example;

import org.example.controller.TransferController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        TransferController controller = new TransferController();

        System.out.println("---- Scenario 1: Successful Transfer ----");
        controller.processTransferRequest("Abi",250.00);

        System.out.println("\n---- Scenario 2: User Error (Over Draft) ----");
        controller.processTransferRequest("Nagendra",15000.0);

        //System.out.println("\n---- Scenario 3: System Error (Triggered Manually for Demo ----");


    }
}
