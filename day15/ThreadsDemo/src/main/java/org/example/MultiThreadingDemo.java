package org.example;

public class MultiThreadingDemo {

    public static void main(String[] args) {

        WorkerThread dbThread = new WorkerThread("Database Backup");
        WorkerThread emailThread = new WorkerThread("Email Sender");
        WorkerThread reportThread = new WorkerThread("Report Generator");


        //lets set the names for each thread
        dbThread.setName("DB-THREAD");
        emailThread.setName("EMAIL-THREAD");
        reportThread.setName("REPORT-THREAD");

        dbThread.start();
        emailThread.start();
        reportThread.start();

        System.out.println("All three threads have been started");
    }
}
