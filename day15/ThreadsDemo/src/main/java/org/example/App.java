package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Main thread started....."+Thread.currentThread().getName() );


        //Lets creat instance of our thread class
        MyThread myThread = new MyThread();

        //Name the thread(optional but good practice
        myThread.setName("Worker-Thread-1");

        //Start the thread
        myThread.start();;

        System.out.println("Main thread continues running......");
    }
}
