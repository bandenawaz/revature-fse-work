package org.revature.employeeapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.revature.employeeapi.model.Employee;
import org.revature.employeeapi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestControllerAdvice
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(
        name = "Employee management",
        description = "Full CRUD operations for employee records"
)
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(employeeService.getAllEmployees(department));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));

    }

    @GetMapping("/salary-range")
    @Tag(name = "Employee Search")
    public ResponseEntity<List<Employee>> getEmployeeBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary
            ){
        return ResponseEntity.ok(employeeService
                .getEmployeeBySalaryRange(minSalary, maxSalary));
    }
}
