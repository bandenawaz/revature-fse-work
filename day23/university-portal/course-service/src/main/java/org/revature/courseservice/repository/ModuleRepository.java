package org.revature.courseservice.repository;

import org.revature.courseservice.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByCourse_CourseIdOrderByOrderIndexAsc(Long courseid);
}
