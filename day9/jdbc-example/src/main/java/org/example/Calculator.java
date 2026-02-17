package org.example;
//
//public interface Calculator {
//
//    int add(int a, int b);
//    int sub(int a, int b);
//}


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

//DON'T do this
@FunctionalInterface
public interface Calculator {

//    int calculate(int a, int b); // only one abstract method
//
//    //We ca nhave static methods
//    default void printResult(int result){
//        System.out.println("Result: " + result);
//    }
//
//    static void info(){
//        System.out.println("This is caculator");
//    }
boolean checkString(String s);




}

//Use this instead
