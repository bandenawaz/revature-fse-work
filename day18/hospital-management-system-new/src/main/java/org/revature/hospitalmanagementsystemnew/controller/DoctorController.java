package org.revature.hospitalmanagementsystemnew.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.hospitalmanagementsystemnew.dto.ApiResponse;
import org.revature.hospitalmanagementsystemnew.model.Doctor;
import org.revature.hospitalmanagementsystemnew.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

    // ═══════════════════════════════════════════════════════════════
    //  @GetMapping — Retrieve data. NEVER modifies anything.
    // ═══════════════════════════════════════════════════════════════

    /*
     * ENDPOINT: GET /api/v1/doctors
     *
     * @GetMapping with NO path → maps to the base URL (/api/v1/doctors)
     *
     * QUERY PARAMETERS (all optional — note defaultValue and required=false):
     * ?specialization=Cardiology  → filter by specialization
     * ?availableOnly=true         → only show available doctors
     * ?maxFee=200                 → only show doctors with fee ≤ 200
     *
     * EXAMPLE CALLS:
     * GET /api/v1/doctors
     * GET /api/v1/doctors?specialization=Cardiology
     * GET /api/v1/doctors?availableOnly=true&maxFee=150
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(required = false) Double maxFee) {

        log.info("GET /api/v1/doctors | specialization={}, availableOnly={}, maxFee={}",
                specialization, availableOnly, maxFee);

        List<Doctor> doctors;

        // If no filters provided, get all. Otherwise, search with filters.
        if (specialization == null && availableOnly == null && maxFee == null) {
            doctors = doctorService.findAllDoctors();
        } else {
            doctors = doctorService.findDoctors(specialization, availableOnly, maxFee);
        }

        return ResponseEntity.ok(
                ApiResponse.successList(
                        "Doctors retrieved successfully",
                        doctors,
                        doctors.size()
                )
        );
    }

    /*
     * ENDPOINT: GET /api/v1/doctors/{id}
     *
     * @PathVariable Long id
     * → Extracts the {id} segment from the URL
     * → Spring automatically converts the String "42" to Long 42
     * → If someone sends GET /api/v1/doctors/abc → 400 Bad Request
     *   (handled by GlobalExceptionHandler.handleTypeMismatch)
     *
     * EXAMPLE: GET /api/v1/doctors/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> getDoctorById(
            @PathVariable Long id) {

        log.info("GET /api/v1/doctors/{}", id);

        Doctor doctor = doctorService.findById(id);
        // If doctor not found, ResourceNotFoundException is thrown → 404

        return ResponseEntity.ok(
                ApiResponse.success("Doctor retrieved successfully", doctor)
        );
    }

    /*
     * ENDPOINT: GET /api/v1/doctors/specializations
     *
     * IMPORTANT: This must be BEFORE the "/{id}" mapping.
     * WHY? If it were AFTER, Spring might think "specializations"
     * is a path variable ID.
     *
     * Actually Spring is smart enough to distinguish literal paths
     * from path variable patterns, but it's good practice to think
     * about ordering.
     */
    @GetMapping("/specializations")
    public ResponseEntity<ApiResponse<List<String>>> getAllSpecializations() {

        log.info("GET /api/v1/doctors/specializations");

        List<String> specializations = doctorService.getAllSpecializations();

        return ResponseEntity.ok(
                ApiResponse.success("Specializations retrieved", specializations)
        );
    }

    /*
     * ENDPOINT: GET /api/v1/doctors/available/count
     * Returns how many doctors are currently available
     */
    @GetMapping("/available/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getAvailableDoctorCount() {

        log.info("GET /api/v1/doctors/available/count");

        long count = doctorService.countAvailableDoctors();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Available doctor count retrieved",
                        Map.of("availableDoctors", count)
                )
        );
    }

    // ═══════════════════════════════════════════════════════════════
    //  @PostMapping — Create a new resource
    // ═══════════════════════════════════════════════════════════════

    /*
     * ENDPOINT: POST /api/v1/doctors
     *
     * @RequestBody Doctor doctor
     * → Jackson reads the JSON from the HTTP request body
     * → Deserializes it into a Doctor Java object
     * → If JSON is malformed → 400 Bad Request (handled by GlobalExceptionHandler)
     *
     * Returns: 201 Created (not 200 OK — this is correct REST practice)
     *
     * REQUEST BODY EXAMPLE:
     * {
     *   "name": "Dr. James Wilson",
     *   "specialization": "Orthopedics",
     *   "licenseNumber": "MED-2024-010",
     *   "email": "james.wilson@hospital.com",
     *   "phoneNumber": "+1-555-0110",
     *   "consultationFee": 180.00,
     *   "qualifications": ["MBBS", "MS Orthopedics"]
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Doctor>> createDoctor(
            @RequestBody Doctor doctor) {

        log.info("POST /api/v1/doctors | Creating: {}", doctor.getDoctorName());

        Doctor created = doctorService.createDoctor(doctor);

        // Return 201 Created — standard for successful resource creation
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor registered successfully", created));
    }

    // ═══════════════════════════════════════════════════════════════
    //  @PutMapping — Replace entire resource
    // ═══════════════════════════════════════════════════════════════

    /*
     * ENDPOINT: PUT /api/v1/doctors/{id}
     *
     * PUT = REPLACE the entire doctor record.
     * The client must send ALL fields, even unchanged ones.
     * Fields missing from the request will be null/default.
     *
     * EXAMPLE: PUT /api/v1/doctors/1
     * Body must contain the COMPLETE doctor representation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        log.info("PUT /api/v1/doctors/{}", id);

        Doctor updated = doctorService.updateDoctor(id, doctor);

        return ResponseEntity.ok(
                ApiResponse.success("Doctor updated successfully", updated)
        );
    }

    // ═══════════════════════════════════════════════════════════════
    //  @PatchMapping — Partial update
    // ═══════════════════════════════════════════════════════════════

    /*
     * ENDPOINT: PATCH /api/v1/doctors/{id}/availability
     *
     * PATCH = Partial update. Only change specific fields.
     * Here we have TWO separate PATCH endpoints:
     * 1. Update availability status
     * 2. Update consultation fee
     *
     * This is better than one giant PATCH that accepts any field,
     * because each endpoint has clear, single-purpose intent.
     *
     * REQUEST BODY: { "available": false }
     */
    @PatchMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Doctor>> updateAvailability(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        log.info("PATCH /api/v1/doctors/{}/availability", id);

        Boolean available = body.get("available");

        if (available == null) {
            // Return 400 Bad Request manually when required field is missing
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Doctor>builder()
                            .success(false)
                            .message("Request body must contain 'available' field (true or false)")
                            .build());
        }

        Doctor updated = doctorService.updateAvailability(id, available);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Doctor availability updated to: " + available,
                        updated
                )
        );
    }

    /*
     * ENDPOINT: PATCH /api/v1/doctors/{id}/fee
     *
     * REQUEST BODY: { "consultationFee": 175.00 }
     */
    @PatchMapping("/{id}/fee")
    public ResponseEntity<ApiResponse<Doctor>> updateConsultationFee(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {

        log.info("PATCH /api/v1/doctors/{}/fee", id);

        Double newFee = body.get("consultationFee");

        if (newFee == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Doctor>builder()
                            .success(false)
                            .message("Request body must contain 'consultationFee' field")
                            .build());
        }

        Doctor updated = doctorService.updateFee(id, newFee);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Consultation fee updated to: $" + newFee,
                        updated
                )
        );
    }

    // ═══════════════════════════════════════════════════════════════
    //  @DeleteMapping — Remove a resource
    // ═══════════════════════════════════════════════════════════════

    /*
     * ENDPOINT: DELETE /api/v1/doctors/{id}
     *
     * Returns: 204 No Content (standard for successful delete with no body)
     * WHY 204 and not 200?
     * → 200 means "success and I'm returning you something"
     * → 204 means "success and there's nothing to return"
     * → After deletion, there IS nothing to return — the resource is gone
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        log.info("DELETE /api/v1/doctors/{}", id);

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
