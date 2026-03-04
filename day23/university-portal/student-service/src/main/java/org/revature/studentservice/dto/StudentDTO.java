package org.revature.studentservice.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private String studentName;
    private String studentEmail;
    private String studentDepartment;
    private int year;
}
