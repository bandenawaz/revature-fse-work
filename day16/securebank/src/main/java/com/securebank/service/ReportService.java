package com.securebank.service;

import com.securebank.db.DBConnection;

public class ReportService {

    //DECLARES a need - doesnot create it
    private DBConnection dbConnection;

    public ReportService(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        System.out.println("Report Service Created. Using " + dbConnection);
    }

    public void generateReport(){
        System.out.println("\n *********  Monthly report  *********");
        String result = dbConnection.query("SELECT * FROM transactions");
        System.out.println("Data: "+result);
    }
}
