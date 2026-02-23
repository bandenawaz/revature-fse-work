package org.example.service;

public class NotificationService {

    private String smtpServer;

    public NotificationService(String smtpServer) {
        this.smtpServer = smtpServer;
        System.out.println("Notification service created. SMTP: " + smtpServer);
    }

    public void sendDepositAlert(String ownerName, double amount) {
        System.out.println("[EMAIL via "+ smtpServer +" ] Dear "+ownerName+
                ", $"+amount+ " was deposited into your account");
    }
}
