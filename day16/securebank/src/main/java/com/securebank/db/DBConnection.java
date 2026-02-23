package com.securebank.db;

public class DBConnection {

    private String host;
    private int port;

    //Constructor
    public DBConnection(String host, int port) {
        this.host = host;
        this.port = port;
        System.out.println("DB connection established "+host+":"+port);
    }

    public String query(String sql){
        return "[DB: "+host+" ] Result: "+sql;
    }
}
