package org.example;

public class MyThread extends Thread{

    @Override
    public void run() {
        super.run();

        System.out.println("Thread name: "+ Thread.currentThread().getName());
        System.out.println("MyThread is running");
    }
}
