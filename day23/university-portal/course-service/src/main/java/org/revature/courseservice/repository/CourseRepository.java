package org.revature.courseservice.repository;

import org.revature.courseservice.entity.Course;
import org.revature.courseservice.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructorId(Long instructorId);

    //Find all active courses
    List<Course> findByIsCourseActiveTrue();
    List<Course> findByTagsName(String tagName);

    //Check if student is enrroled
    List<Course> findByEnrolledStudentIdsContaining(Long studentId);
}
