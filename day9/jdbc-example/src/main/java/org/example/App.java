package org.example;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Hello world!
 *
 */
public class App 
{

    //Step1: Define Connection parameters
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root@123";
    public static void main( String[] args )
    {
//        System.out.println( "First JDBC Application" );
//
//        //Step 2-6: inside try with resources (auto-closes connection)
//        try(
//                //Step: Establish the connection
//                Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
//
//                //Step 3: Create a statement
//                Statement statement = connection.createStatement();
//                ){
//                    System.out.println("Connected to the database successfully");
//
//                    //Step 4: Execute SQL query
//            String sqlQuery = "select id, name, balance from accounts";
//            ResultSet resultSet = statement.executeQuery(sqlQuery);
//
//            System.out.println("\n *======* Account List *=====*");
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                double balance = resultSet.getDouble("balance");
//                System.out.printf("ID: %d    | Name: %-20s  | Balance: $%.2f%n", id, name,balance);
//            }
//
//            //Step 6: Resultset closed manually
//            resultSet.close();
////
//
////
//            String sql = "Select * from doctors wher username = ? and password =?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1,DB_USER);
//            preparedStatement.setString(2,DB_PASSWORD);
//            preparedStatement.setString(0,DB_USER);
//
//            //' OR '1'='1' --   This is treated as literal string not sql
//
//        } catch (SQLException e) {
//
//            System.err.println("SQL Error: " + e.getMessage());
//            e.printStackTrace();
//        }

//        Predicate<Integer> isEven = num -> num % 2 == 0;
//        boolean result = isEven.test(10);
//        System.out.println(result);
//
//        Function<String, Integer> strLength = str -> str.length();
//        Integer length = strLength.apply("Nawaz");
//        System.out.println(length);




    }


    

}
