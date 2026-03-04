package org.revature.courseservice.dto;

import lombok.Data;

@Data
public class ModuleDTO {
    private String moduleName;
    private String moduleContent;
    private int orderIndex;
    private int durationInHours;

}
