package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsDemo {
    public static void main(String[] args) {


        List<String> names = Arrays.asList("Alice","Bob","Charlie","David","Eve");

        //Without STREAMS
        //Find names with more than 3 letters, convert to upper case, collect them
//        List<String> result = new ArrayList<>();
//        for (String name : names){
//            if (name.length() > 3){
//                String upperCaseName = name.toUpperCase();
//                result.add(upperCaseName);
//            }
//        }
//        System.out.println(result);

        //WITH STREAM
        List<String> result = names.stream()
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(result);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9,10);
        Stream<Integer> stream = numbers.stream()  //data source
                .filter(number ->{
                    System.out.println("Filtering: "+number);
                    return  number > 5;
                })
                .map(number ->{
                    System.out.println("Mapping: "+number);
                    return number * 2;
                });

        System.out.println("Stream created but nothing printed...");
        //Now lets execute using terminal operation
        List<Integer> res =  stream.collect(Collectors.toList());
        //System.out.println("Stream created but nothing printed...");


    }
}
