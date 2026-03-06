package org.revature.studentservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.studentservice.repository.StudentRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("studentDatabase")
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthIndicator implements HealthIndicator {

    private final StudentRepository studentRepository;

    @Override
    public Health health() {
        try {
            // Attempt DB query
            long studentCount = studentRepository.count();

            if (studentCount == 0) {
                return Health.up()
                        .withDetail("status", "Connected")
                        .withDetail("studentCount", studentCount)
                        .withDetail("warning", "No students found - expected during initialization setup")
                        .build();
            }

            return Health.up()
                    .withDetail("status", "Connected")
                    .withDetail("studentCount", studentCount)
                    .withDetail("database", "university_db")
                    .withDetail("message", "All systems are operational")
                    .build();

        } catch (Exception e) {

            log.error("Database health check failed: {}", e.getMessage());

            return Health.down()
                    .withDetail("status", "Disconnected")
                    .withDetail("error", e.getMessage())
                    .withDetail("database", "university_db")
                    .withDetail("action", "Check MySQL connection and credentials")
                    .build();
        }
    }
}