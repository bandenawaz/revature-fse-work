package org.revature.studentservice.controller;

import lombok.RequiredArgsConstructor;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.entity.Student;
import org.revature.studentservice.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")   //BASE URL for all endpoints
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    //Create a student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody StudentDTO studentDTO) {
        Student newStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
    }

    //Fetch all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    //Get student by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    //Update Student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    //Delete the student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();  //204 no content
    }
}
