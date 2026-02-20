package org.example;

public class WorkerThread extends Thread {

    private String taskName;

    public WorkerThread(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        for( int i = 0; i < 10; i++ ) {
            System.out.println(taskName + " - Step "+ i + " - Thread: "
            +Thread.currentThread().getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        }
        System.out.println(taskName + " - Finished");
    }
}
