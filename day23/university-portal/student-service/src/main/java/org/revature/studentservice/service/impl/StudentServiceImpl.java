package org.revature.studentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;
import org.revature.studentservice.repository.StudentRepository;
import org.revature.studentservice.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student createStudent(StudentDTO studentDTO) {
        /**
         * Business Rule: No dupliucate emails allowed
         * email: unique
         */
        studentRepository.findByStudentEmail(studentDTO.getStudentEmail())
                .ifPresent(student -> {
                    throw new RuntimeException("Email already in exists "
                            +studentDTO.getStudentEmail());
                });
        Student student = new Student();
        student.setStudentName(studentDTO.getStudentName());
        student.setStudentEmail(studentDTO.getStudentEmail());
        student.setStudentDepartment(studentDTO.getStudentDepartment());
        student.setYear(studentDTO.getYear());
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: "+id));
    }

    @Override
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = getStudentById(id);
        existingStudent.setStudentName(studentDTO.getStudentName());
        existingStudent.setStudentEmail(studentDTO.getStudentEmail());
        existingStudent.setStudentDepartment(studentDTO.getStudentDepartment());
        existingStudent.setYear(studentDTO.getYear());
        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);

    }
}
