package org.revature.employeeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.employeeapi.exception.BusinessRuleException;
import org.revature.employeeapi.exception.ResourceNotFoundException;
import org.revature.employeeapi.model.Employee;
import org.revature.employeeapi.respository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            log.error("Employee with email {} already exists", employee.getEmail());
            throw new BusinessRuleException(
                    "Employee with email " + employee.getEmail() + " already exists"
            );
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(String department) {
        if (department != null) {
            return employeeRepository.findByDepartment(department);
        }
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", employeeId)
        );
    }

    //update
    //delete

    public List<Employee> getEmployeeBySalaryRange(BigDecimal min, BigDecimal max) {
        return employeeRepository.findBySalaryBetween(min, max);
    }
}
