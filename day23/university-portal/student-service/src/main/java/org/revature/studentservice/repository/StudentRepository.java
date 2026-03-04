package org.revature.studentservice.repository;

import org.revature.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    //Select * from students where email =?
    Optional<Student> findByStudentEmail(String studentEmail);

    //Select * from students where department = ?
    List<Student> findByStudentDepartment(String studentDepartment);
}
