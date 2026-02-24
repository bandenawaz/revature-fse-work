package com.securebank.config;

import com.securebank.db.DatabaseConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifeCycleConfig {

    @Bean
    public DatabaseConnectionPool dbConnectionPool() {
        // Spring calls constructor -> @PostConstruct -> then register @PreDestroy
        return new DatabaseConnectionPool("db.securebank.com",3);
    }


}
