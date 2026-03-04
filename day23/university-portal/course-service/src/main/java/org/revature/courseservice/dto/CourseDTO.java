package org.revature.courseservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseDTO {
    private String courseTitle;
    private String courseDescription;
    private int courseCredits;
    private Long instructorId;
    private List<String> tagNames;
    private List<ModuleDTO> modules;
}
