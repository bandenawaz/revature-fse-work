package org.revature.studentservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;
import org.revature.studentservice.repository.StudentRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Pure Mockito, no Spring Context
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository; //Fake repository

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;  //Real service with fake repos injected

    private StudentDTO studentDTO;
    private Student student;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
        studentDTO.setStudentName("Arjun Sharma");
        studentDTO.setStudentEmail("arjun@uni.edu");
        studentDTO.setStudentDepartment("CSE");
        studentDTO.setYear(2);

        student = new Student(
                1L, "Arjun Sharma",
                "arjun@uni.edu","CSE",2
        );

    }

    @Test
    void createStudent_WhenEmailNotExiste_ShouldSaveAndReturn() {
        //ARRANGE
        when(studentRepository.findByStudentEmail(studentDTO.getStudentEmail()))
                .thenReturn(Optional.empty());  //No existing student with this email

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        //ACT
        Student result = studentServiceImpl.createStudent(studentDTO);

        //ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getStudentName()).isEqualTo(studentDTO.getStudentName());
        assertThat(result.getStudentEmail()).isEqualTo(studentDTO.getStudentEmail());

        //Verify repository was actually called
        verify(studentRepository, times(1)).save(any(Student.class));

    }

    //getStudentByIdWhenExistShouldReturnStudent
    //getStudentByIdWhenNotExistsShouldThrowException

}