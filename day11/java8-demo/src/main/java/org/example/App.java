package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        MathOperation add = (a, b) -> a + b;
        MathOperation sub = (a, b) -> a - b;
        MathOperation mul = (a, b) -> a * b;

        //Use them
        System.out.println("10 + 5 = "+ add.operate(10,5));
        System.out.println("10 - 5 = "+ sub.operate(10,5));
        System.out.println("10 * 5 = "+ mul.operate(10,5));
    }
}
