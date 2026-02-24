package com.securebank;

import com.securebank.config.LifeCycleConfig;
import com.securebank.db.DatabaseConnectionPool;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanLifeCycleDemo {
    public static void main(String[] args) {

        System.out.println("BeanLifeCycleDemo started");

        System.out.println("Starting Container");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(LifeCycleConfig.class);

        System.out.println("\nApplication Running\n");

        DatabaseConnectionPool dbConnectionPool = context.getBean(DatabaseConnectionPool.class);
        String conn1 = dbConnectionPool.getConnection();
        String conn2 = dbConnectionPool.getConnection();
        System.out.println(" (doing database work with conn1 and conn2)");
        dbConnectionPool.returnConnection(conn1);
        dbConnectionPool.returnConnection(conn2);

        System.out.println("\n Shuttingdown Container\n");
        context.close();
        System.out.println("BeanLifeCycleDemo stopped");

    }
}
