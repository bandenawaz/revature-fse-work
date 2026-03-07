package org.revature.courseservice.dto;

import lombok.Data;

@Data
public class StudentDTO {

    private Long id;
    private String name;
    private String email;
    private String department;
    private int academicYear;

    // This field is set by StudentClientFallback when student-service is down
    // Normal responses will never have this as "N/A"
    private String status = "ACTIVE";
}