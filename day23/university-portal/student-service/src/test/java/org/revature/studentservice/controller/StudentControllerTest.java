package org.revature.studentservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;
import org.revature.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)  // Load only web layer, no db
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;   //Simulates HTTP requests without a server

    @MockitoBean
    private StudentService studentService;  //Creates a Fake Student Service

    @Autowired
    private ObjectMapper objectMapper;  //Converts Java objects to JSON

    @Test
    @DisplayName("createStudent should return 201 status")
    void createStudentShouldReturn201Status() throws Exception {
        //Arrange: Prepare fake input and fake output
        StudentDTO  studentDTO = new StudentDTO();
        studentDTO.setStudentName("Arjun Sharma");
        studentDTO.setStudentEmail("arjun@university.edu");
        studentDTO.setStudentDepartment("Computer Science");
        studentDTO.setAcademicYear(2);


        Student savedStudent = new Student(
                1L, "Arjun Sharma","arjun@university.edu",
                "Computer Science",2
        );

        //WHEN service.createStudent() is called with any DTO, return savedStudent
        when(studentService.createStudent(any(
                StudentDTO.class
        ))).thenReturn(savedStudent);

        //ACT + ASSERT: Make a POST request and verify response
        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.studentName").value("Arjun Sharma"))
                .andExpect(jsonPath("$.studentEmail").value("arjun@university.edu"));
    }

    @Test
    @DisplayName("getAllStudents should return 200 status with List of students")
    void getAllStudents_ShouldReturn200Status_WithList() throws Exception {
        //ARRANGE
        List<Student>  students = List.of(
                new Student(1L, "Arjun","arjun@uni.edu",
                        "CSE",2),
                new Student(2L, "Priya","priya@uni.edu",
                        "ECE",3)

                );

        when(studentService.getAllStudents()).thenReturn(students);

        //ACT + ASSERT
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studentName")
                        .value("Arjun"))
                .andExpect(jsonPath("$[1].studentName")
                        .value("Priya"));
    }

    @Test
    void getStudentByID_NotFound_ShouldReturn500() throws Exception {

        //Arrange: make service throw an Exception
        when(studentService.getStudentById(99L))
                .thenThrow(new RuntimeException("Student not found with Id: 99L"));

        //ACT + ASSERT
        mockMvc.perform(get("/api/v1/students/99"))
                .andExpect(status().isInternalServerError());
    }

    //update
    //delete
    //failing tests
}