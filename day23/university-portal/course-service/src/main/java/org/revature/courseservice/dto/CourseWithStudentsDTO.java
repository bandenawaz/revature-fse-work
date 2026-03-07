package org.revature.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.revature.courseservice.entity.Course;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWithStudentsDTO {

    private Course course;

    private List<StudentDTO> enrolledStudents = new ArrayList<>();

    // Convenience fields derived from the course — avoids the client
    // having to dig into the nested Course object for common info
    private String courseTitle;
    private int totalEnrolled;
    private int remainingSeats;
    private boolean isFull;

    // This constructor matches Usage 2 in the service layer
    public CourseWithStudentsDTO(Course course, List<StudentDTO> enrolledStudents) {
        this.course = course;
        this.enrolledStudents = enrolledStudents != null
            ? enrolledStudents
            : new ArrayList<>();

        // Auto-populate convenience fields from the course object
        if (course != null) {
            this.courseTitle    = course.getCourseTitle();
            this.totalEnrolled  = course.getEnrolledStudentIds().size();
            this.remainingSeats = course.getMaxSeats() - this.totalEnrolled;
            this.isFull         = this.remainingSeats <= 0;
        }
    }
}