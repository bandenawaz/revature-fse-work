package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankTransfer {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root@123";


    public static void transferFunds(int fromAccountId, int toAccountId, double amount) {

        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try(
                //Step: Establish the connection
                Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

                ){
            PreparedStatement debitStmnt = connection.prepareStatement(debitSql);
            PreparedStatement creditStmnt = connection.prepareStatement(creditSql);
            // -- Debit from the source account --
            debitStmnt.setDouble(1, amount);
            debitStmnt.setInt(2, fromAccountId);
            debitStmnt.setDouble(3, amount);

            int rowsDebited =  debitStmnt.executeUpdate();

            if(rowsDebited == 0) {
                System.out.println("Transfer Failed: Insufficient Balance");
                return;
            }

            // -- Credit  the destination account --
            creditStmnt.setDouble(1, amount);
            creditStmnt.setInt(2, toAccountId);

            creditStmnt.executeUpdate();

            System.out.printf("$%.2f transferred from Account %d to Account %d%n", amount, fromAccountId,toAccountId);

        }catch (SQLException e){
            System.err.println("Transfer Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        transferFunds(101,102,50000.00);

    }
}
