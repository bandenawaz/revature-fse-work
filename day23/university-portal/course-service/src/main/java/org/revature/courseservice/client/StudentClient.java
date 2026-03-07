package org.revature.courseservice.client;

import org.revature.studentservice.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * name = spring.application.name of the target service )resolved via Eureka)
 * fallback = what to do if student-service is DOWN (Circuit Breaker fallback)
 */
@FeignClient(
        name = "student-service",
        fallback = StudentClientFallback.class
)
public interface StudentClient {

    /**
     * This looks EXACTLY like the StudentController method signature
     * Feign reads this and generates the HTTP call automatically
     */
    @GetMapping("/api/v1/students")
    List<StudentDTO> getAllStudents(
            @RequestHeader("Authorization") String authToken
    );

    @GetMapping("/api/v1/students/{id}")
    StudentDTO getStudentById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authToken
    );

    @GetMapping("/api/v1/students/department/{department}")
    List<StudentDTO> getStudentsByDepartment(
            @PathVariable("department") String department,
            @RequestHeader("Authorization") String authToken
    );



}
