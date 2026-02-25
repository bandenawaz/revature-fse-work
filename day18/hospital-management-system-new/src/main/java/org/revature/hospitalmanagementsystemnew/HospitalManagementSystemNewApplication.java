package org.revature.hospitalmanagementsystemnew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HospitalManagementSystemNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementSystemNewApplication.class, args);
    }

}
