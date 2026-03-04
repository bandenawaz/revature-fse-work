package org.revature.courseservice.repository;

import org.revature.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructor_InstructorId(Long instructorId);

    //Find all active courses
    List<Course> findByIsCourseActiveTrue();
    List<Course> findByTags_TagName(String tagName);

    //Check if student is enrolled
    List<Course> findByEnrolledStudentIdsContaining(Long studentId);
}
