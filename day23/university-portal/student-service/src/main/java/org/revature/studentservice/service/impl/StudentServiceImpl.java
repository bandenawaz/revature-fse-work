package org.revature.studentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.studentservice.actuator.StudentMetricsService;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;
import org.revature.studentservice.repository.StudentRepository;
import org.revature.studentservice.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMetricsService studentMetricsService;

    @Override
    public Student createStudent(StudentDTO studentDTO) {

        try {
            //Wrap the entire creation in timer
            return studentMetricsService.timeStudentCreation(() ->{

                /**
                 * Business Rule: No duplicate emails allowed
                 * email: unique
                 */
                studentRepository.findByStudentEmail(studentDTO.getStudentEmail())
                        .ifPresent(student -> {
                            studentMetricsService.recordDuplicateEmail();
                            throw new RuntimeException("Email already in exists "
                                    + studentDTO.getStudentEmail());
                        });
                Student student = new Student();
                student.setStudentName(studentDTO.getStudentName());
                student.setStudentEmail(studentDTO.getStudentEmail());
                student.setStudentDepartment(studentDTO.getStudentDepartment());
                student.setAcademicYear(studentDTO.getAcademicYear());

                Student savedStudent = studentRepository.save(student);
                //Record successfull creation with student's year
                studentMetricsService.recordStudentCreated(studentDTO.getAcademicYear());
                return savedStudent;
            });

        }catch (RuntimeException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Failed to create student",e);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseGet(() -> {
                    studentMetricsService.recordStudentNotFound();
                    log.warn("Student with id {} was not found", id);
                   throw  new RuntimeException("Student not found with id: " + id);
                });
    }

    @Override
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = getStudentById(id);
        existingStudent.setStudentName(studentDTO.getStudentName());
        existingStudent.setStudentEmail(studentDTO.getStudentEmail());
        existingStudent.setStudentDepartment(studentDTO.getStudentDepartment());
        existingStudent.setAcademicYear(studentDTO.getAcademicYear());
        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);

    }
}
