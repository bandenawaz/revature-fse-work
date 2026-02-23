package org.example.service;

public class AccountService {

    private NotificationService notificationService;

    public AccountService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void processDeposit(String ownerName, double amount) {
        System.out.println("Deposited $"+amount);
        notificationService.sendDepositAlert(ownerName, amount);
    }
}
