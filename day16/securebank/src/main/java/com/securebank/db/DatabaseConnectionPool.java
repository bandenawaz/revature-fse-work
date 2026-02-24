package com.securebank.db;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionPool {

    private String dbHost;
    private int maxConnections;
    private List<String> pool = new ArrayList<>();
    private boolean ready = false;

    //STAGE 1: CONSTRUCTOR
    // Spring calls this first
    public DatabaseConnectionPool(String dbHost, int maxConnections) {
        this.dbHost = dbHost;
        this.maxConnections = maxConnections;
        System.out.println("[lifecycle 1] Constructor: object born. Host: "+dbHost );
        System.out.println(" WARNING: Cannot use DB yet - @PostConstruct not run!");
    }

    //STAGE 2: @POSTCONSTRUCT
    /*
    * called AFTER all dependencies injected
    * Safe to initialize here
     */
    @PostConstruct
    public void initialize(){
        System.out.println("[lifecycle 2] @POSTCONTRUCT: opening connections....." );

        for (int i = 0; i <= maxConnections; i++){

            String conn = "Conn-"+ i+ "@"+dbHost;
            pool.add(conn);
            System.out.println(" Opened: "+conn);
        }
        ready = true;
        System.out.println("POOL ready with "+pool.size()+" connections");
    }

    // Application use - normal operations
    public String getConnection(){
        if (!ready) throw new IllegalStateException("Pool not initialized");
        if (pool.isEmpty()) return "ExtraConn@"+dbHost;

        String conn = pool.remove(0);
        System.out.println(" -> Lent: "+conn+ "| Remaining: "+pool.size());
        return conn;
    }

    public void returnConnection(String conn){
        pool.add(conn);
        System.out.println(" <- Returned: "+conn+ " PoolSize: "+pool.size());
    }

    //STAGE 3: @PreDestory
    /*
    * Called before Spring destroys
    * Release resources here
     */
    @PreDestroy
    public void shutdown(){
        System.out.println("[lifecycle 3] @PreDestroy: closing all connections....");
        for (String conn : pool){
            System.out.println(" Closed: "+conn);
        }
        pool.clear();
        ready = false;
        System.out.println("POOL SHUTDOWN complete");
    }
}
