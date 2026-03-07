package org.revature.courseservice.client;

import lombok.extern.slf4j.Slf4j;
import org.revature.studentservice.dto.StudentDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
/**
 * Spring creates this bean and uses it when student-service is unavailable
 */
public class StudentClientFallback implements StudentClient{
    @Override
    public List<StudentDTO> getAllStudents(String authToken) {
        log.warn("FALLBACK: student-service is unavailable for getAllStudents()");
        return Collections.emptyList();
    }

    @Override
    public StudentDTO getStudentById(Long id, String authToken) {
        log.warn("FALLBACK: student-service is unavailable for student id: {}",id);

        //Return a placeholdr instead of throwing an exception
        StudentDTO fallback = new StudentDTO();
        fallback.setStudentName("Student data is temporarily unavailable");
        fallback.setStudentEmail("N/A");
        fallback.setStudentDepartment("N/A");
        return fallback;
    }

    @Override
    public List<StudentDTO> getStudentsByDepartment(String department, String authToken) {
        log.warn("FALLBACK: student-service is unavailable for student department: {}",department);
        return Collections.emptyList();
    }
}
