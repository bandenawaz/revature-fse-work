package org.revature.courseservice.controller;

import lombok.RequiredArgsConstructor;
import org.revature.courseservice.dto.CourseDTO;
import org.revature.courseservice.dto.InstructorDTO;
import org.revature.courseservice.dto.ModuleDTO;
import org.revature.courseservice.entity.Course;
import org.revature.courseservice.entity.Instructor;
import org.revature.courseservice.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseDTO courseDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(courseDTO));

    }

    @PostMapping("/instructor")
    public ResponseEntity<Instructor> addInstructor(
            @RequestBody InstructorDTO instructorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createInstructor(instructorDTO));

    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/by-tag/{tagName}")
    public ResponseEntity<List<Course>> getCourseByTag(@PathVariable String tagName) {
        return ResponseEntity.ok(courseService.getAllCoursesByTag(tagName));
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<Course> addModule(@PathVariable Long courseId, @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.addModuleToCourse(courseId, moduleDTO));
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Course> addEnrollment(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.enrolledStudent(courseId, studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
