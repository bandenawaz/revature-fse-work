package org.revature.employeeapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.revature.employeeapi.model.Employee;
import org.revature.employeeapi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(
            summary = "Register a new employee",
            //  ↑ SHORT. Appears in the collapsed list view.
            //    Answer: "what does this endpoint do in 5-7 words?"

            description = """
            Creates a new employee record in the system.
            
            **Business Rules:**
            - Email address must be unique across all employees
            - Salary must be greater than zero
            - Hire date defaults to today if not provided
            - New employees are set to 'active' by default
            
            **Permissions Required:** `ROLE_HR_MANAGER` or `ROLE_ADMIN`
            """,
            //  ↑ DETAILED. Appears when endpoint is expanded.
            //    Use Markdown formatting.
            //    Include: business rules, validation, permissions, special cases.
            //    This is the narrative that explains the MEANING of the operation.

            operationId = "createEmployee",
            //  ↑ A unique string identifier for this operation.
            //    Used by code generators (Swagger Codegen, OpenAPI Generator)
            //    to name the generated client method.
            //    Convention: camelCase verb + noun.
            //    If not specified, SpringDoc generates one automatically.

            tags = {"Employee Management"}
            //  ↑ Can override the class-level tag here
    )
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(employee));
    }
    // ══════════════════════════════════════════════════════════════
    //  GET ALL
    // ══════════════════════════════════════════════════════════════

    @Operation(
            summary = "Retrieve all employees with optional department filter",
            description = """
            Returns a list of all active employees. Optionally filter by department.
            
            Returns an empty array (not 404) if no employees match the filter.
            Only active employees are returned. Deactivated employees are excluded.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Employee.class),
                            //  ↑ @Schema points to your Employee class.
                            //    Swagger introspects the class and shows all fields,
                            //    their types, and any validation annotations as documentation.

                            examples = @ExampleObject(
                                    name = "Filtered by Engineering department",
                                    summary = "Sample response when filtering by department=Engineering",
                                    value = """
                        [
                          {
                            "id": 1,
                            "firstName": "Alice",
                            "lastName": "Chen",
                            "email": "alice.chen@techcorp.com",
                            "department": "Engineering",
                            "salary": 95000.00,
                            "hireDate": "2022-03-15",
                            "active": true
                          },
                          {
                            "id": 7,
                            "firstName": "Bob",
                            "lastName": "Singh",
                            "email": "bob.singh@techcorp.com",
                            "department": "Engineering",
                            "salary": 87000.00,
                            "hireDate": "2023-01-10",
                            "active": true
                          }
                        ]
                        """
                                    //  ↑ Example JSON shown in Swagger UI.
                                    //    This is ENORMOUSLY helpful for consumers.
                                    //    They see exactly what format to expect.
                                    //    No more guessing field names or data formats.
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                        {
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Invalid department name format",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(employeeService.getAllEmployees(department));
    }

    // ══════════════════════════════════════════════════════════════
    //  GET BY ID
    // ══════════════════════════════════════════════════════════════

    @Operation(
            summary = "Get a specific employee by their ID",
            description = "Returns complete details for a single employee. " +
                    "Returns 404 if the employee ID does not exist or the employee has been deactivated."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found and returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "firstName": "Alice",
                          "lastName": "Chen",
                          "email": "alice.chen@techcorp.com",
                          "department": "Engineering",
                          "salary": 95000.00,
                          "hireDate": "2022-03-15",
                          "active": true
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                        {
                          "status": 404,
                          "error": "Not Found",
                          "message": "Employee with ID 999 was not found",
                          "path": "/api/v1/employees/999",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized — valid API key required",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                        {
                          "status": 401,
                          "error": "Unauthorized",
                          "message": "Missing or invalid API key. Include header: X-API-Key"
                        }
                        """
                            )
                    )
            )
    })
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
