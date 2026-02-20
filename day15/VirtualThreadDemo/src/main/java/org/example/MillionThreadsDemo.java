package org.example;

import java.util.concurrent.Executors;

public class MillionThreadsDemo {

    public static void main(String[] args) throws InterruptedException {

        long startTime =  System.currentTimeMillis();

        //Create an executor that creates a new virtual thread per task
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            //Submit 1 million tasks
            for( int i = 0; i < 1_000_000; i++ ) {
                int taskId = i;

                executor.submit(() -> {
                    //Simulate I/O operation (e.g., DB query taking 1 second)
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                });
            }

        }//Executor auto-closes abd waits for all tasks
        long endTime =  System.currentTimeMillis();
        System.out.println("1 Million virtual thread tasks completed in: "+
                (endTime - startTime) / 1000 +" seconds");


    }
}
