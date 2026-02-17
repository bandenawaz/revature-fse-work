package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankTransferwithTransaction {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root@123";




    public static void transferFunds(int fromAccountId, int toAccountId, double amount) {

        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        Connection connection = null;

        try
        {
            //Step: Establish the connection
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            //Step 1: Disable the auto-commit - Begin Transaction
            connection.setAutoCommit(false);

            //Step 2: Execute the debit
            PreparedStatement debitStmnt = connection.prepareStatement(debitSql);
            PreparedStatement creditStmnt = connection.prepareStatement(creditSql);
            // -- Debit from the source account --
            debitStmnt.setDouble(1, amount);
            debitStmnt.setInt(2, fromAccountId);
            debitStmnt.setDouble(3, amount);

            int rowsDebited =  debitStmnt.executeUpdate();

            if(rowsDebited == 0) {
                //Business rule violated - not enough balance
                connection.rollback();  // Rollback even though nothing changed yet
                System.out.println("Insufficient Balance: Transaction rolled back");
                return;
            }

            //Step 3: execute the credit

            // -- Credit  the destination account --
            creditStmnt.setDouble(1, amount);
            creditStmnt.setInt(2, toAccountId);

            creditStmnt.executeUpdate();

            //Step 4: Both succeeded - commit permanantly
            connection.commit();

            System.out.printf("$%.2f transferred from Account %d to Account %d%n", amount, fromAccountId,toAccountId);

        }catch (SQLException e){
            //Step 5: Any SQL error - rollback everything
            System.err.println("Transfer Error: " + e.getMessage());

            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction rolled back, no data changed");
                }catch (SQLException rollbackEx){
                    System.err.println("Rollback Error: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                }catch (SQLException closeEx){
                    System.err.println("Connection close failed: " + closeEx.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) {

        transferFunds(101,102,50000.00);

    }
}
