package org.revature.studentservice.service;

import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(StudentDTO studentDTO);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student updateStudent(Long id,StudentDTO studentDTO);
    void deleteStudent(Long id);
}
