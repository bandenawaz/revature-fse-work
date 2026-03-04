package org.revature.courseservice.service;

import org.revature.courseservice.dto.CourseDTO;
import org.revature.courseservice.dto.InstructorDTO;
import org.revature.courseservice.dto.ModuleDTO;
import org.revature.courseservice.entity.Course;
import org.revature.courseservice.entity.Instructor;

import java.util.List;

public interface CourseService {
    Instructor createInstructor(InstructorDTO instructorDTO);
    Course createCourse(CourseDTO courseDTO);
    Course getCourseById(Long id);
    List<Course> getAllCourses();
    List<Course> getAllCoursesByTag(String tagName);
    Course addModuleToCourse(Long courseId,ModuleDTO moduleDTO);
    Course enrolleStudent(Long courseId,Long studentId);
    void deleteCourse(Long courseId);
}
