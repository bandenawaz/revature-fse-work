package org.revature.studentservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.revature.studentservice.repository.StudentRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("studentDatabase")
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthIndicator implements HealthIndicator {

    private final StudentRepository studentRepository;
    @Override
    public @Nullable Health health() {
        try{
            //Attempt to count students - if DB is dead, this throws
            long studentCount = studentRepository.count();

            //You can add custom business logic here
            // e.g., if studentCount == 0, during semester, something is wrong
            if(studentCount == 0){
                return Health.up()
                        .withDetail("status", "Connected")
                        .withDetail("studentCount", studentCount)
                        .withDetail("warning", "No students found - expected during initialization setup")
                        .build();
            }
            return Health.up()
                    .withDetail("status", "Connected")
                    .withDetail("studentCount", studentCount)
                    .withDetail("database","university_db")
                    .withDetail("message", "All systems are operational")
                    .build();
        }catch (Exception e){
            log.error("Database health check failed: {}",e.getMessage());

            //Health.down() signals to Kubernetes/loadBalance:
            //Stop sending traffic to me
            return Health.down()
                    .withDetail("status","Disconnected")
                    .withDetail("error", e.getMessage())
                    .withDetail("database","university_db")
                    .withDetail("action","Check MySQL connection and credentials")
                    .build();
        }

    }
}
