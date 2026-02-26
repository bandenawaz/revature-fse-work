package org.revature.employeeapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Standard error response.
 * EVERY error response will have this exact shape.
 *
 * Example JSON output for a 404:
 * {
 *   "success": false,
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Doctor with ID 999 not found",
 *   "path": "/api/doctors/999",
 *   "timestamp": "2024-11-15T10:30:00"
 * }
 *
 * Example for validation failure (422):
 * {
 *   "success": false,
 *   "status": 422,
 *   "error": "Validation Failed",
 *   "message": "Request data is invalid",
 *   "fieldErrors": ["name: must not be blank", "email: must be valid email"],
 *   "path": "/api/doctors",
 *   "timestamp": "2024-11-15T10:30:00"
 * }
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Boolean success;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<String> fieldErrors;  // Only present for validation errors
}