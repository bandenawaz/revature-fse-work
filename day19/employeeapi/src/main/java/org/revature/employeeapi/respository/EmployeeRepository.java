package org.revature.employeeapi.respository;

import org.revature.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //Just declare the method signature - Spring writes the query for us
    List<Employee> findByDepartment(String department);
    List<Employee> findBySalaryBetween(BigDecimal min, BigDecimal max);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartmentAndActive(String department, Boolean active);
    boolean existsByEmail(String email);
}
