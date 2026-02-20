package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {

        // --- Traditional Platform Thread
        Thread platformThread =
                Thread.ofPlatform()
                                .name("Platform-Thread")
                                        .start(() ->{
                                            System.out.println("Platform-Thread started:" +
                                                    Thread.currentThread().isVirtual());
                                        });
        // --- Virtual Thread
        Thread virtualThread = Thread.ofVirtual()
                        .name("VIRTUAL-THREAD")
                                .start(() ->{
                                    System.out.println("VIRTUAL-Thread started: "+Thread.currentThread().isVirtual());
                                });

        platformThread.join();
        virtualThread.join();
    }
}
