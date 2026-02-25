package org.revature.hospitalmanagementsystemnew.service;

import lombok.extern.slf4j.Slf4j;
import org.revature.hospitalmanagementsystemnew.exception.BusinessRuleException;
import org.revature.hospitalmanagementsystemnew.exception.DuplicateResourceException;
import org.revature.hospitalmanagementsystemnew.exception.ResourceNotFoundException;
import org.revature.hospitalmanagementsystemnew.model.Doctor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorService {

    private final Map<Long, Doctor> doctorDatabase = new HashMap<Long, Doctor>();
    private final AtomicLong idSequence = new AtomicLong(1);

    //Lets pre populate with some sample data when this service is created
    {
        Doctor zayaan = Doctor.builder()
                .doctorId(idSequence.getAndIncrement())
                .doctorName("Dr Zayaan Bagwan")
                .doctorSpecialization("Cardiology")
                .doctorLicenseNumber("MED-2025-001")
                .doctorEmail("zayaan.bagwan@cityhospital.com")
                .doctorPhoneNumber("+1-555-001")
                .doctorConsultationFee(1000.0)
                .joiningDate(LocalDate.of(2025, 11, 24))
                .doctorAvailable(true)
                .doctorQualifications(List.of("MBBS", "MD", "FACC"))
                .build();

        Doctor azmat = Doctor.builder()
                .doctorId(idSequence.getAndIncrement())
                .doctorName("Dr Azmat Bagwan")
                .doctorSpecialization("Neurology")
                .doctorLicenseNumber("MED-2025-008")
                .doctorEmail("azmat.bagwan@cityhospital.com")
                .doctorPhoneNumber("+1-555-101")
                .doctorConsultationFee(1000.0)
                .joiningDate(LocalDate.of(2016, 7, 13))
                .doctorAvailable(true)
                .doctorQualifications(List.of("MBBS", "DM Neurology"))
                .build();

        doctorDatabase.put(zayaan.getDoctorId(), zayaan);
        doctorDatabase.put(azmat.getDoctorId(), azmat);

        log.info("Doctor Service initialized successfully with {} sample doctors",
                doctorDatabase.size());

    }
        // ─── READ ALL ─────────────────────────────────────────────────────────────
        public List<Doctor> findAllDoctors() {
        log.info("Fetching all {} doctors", doctorDatabase.size());
        return new ArrayList<>(doctorDatabase.values());
    }

        // ─── READ WITH FILTERS ────────────────────────────────────────────────────
        public List<Doctor> findDoctors(String specialization, Boolean availableOnly,
            Double maxFee) {
        log.info("Searching doctors | specialization={}, availableOnly={}, maxFee={}",
                specialization, availableOnly, maxFee);

        return doctorDatabase.values().stream()
                // Filter by specialization if provided
                .filter(d -> specialization == null ||
                        d.getDoctorSpecialization().equalsIgnoreCase(specialization))
                // Filter by availability if requested
                .filter(d -> !Boolean.TRUE.equals(availableOnly) ||
                        Boolean.TRUE.equals(d.getDoctorAvailable()))
                // Filter by max fee if provided
                .filter(d -> maxFee == null || d.getDoctorConsultationFee() <= maxFee)
                .collect(Collectors.toList());
    }

        // ─── READ ONE ─────────────────────────────────────────────────────────────
        public Doctor findById(Long id) {
        log.info("Fetching doctor with id: {}", id);
        Doctor doctor = doctorDatabase.get(id);
        if (doctor == null) {
            // This exception propagates to GlobalExceptionHandler → 404 response
            throw new ResourceNotFoundException("Doctor", id);
        }
        return doctor;
    }

        // ─── CREATE ───────────────────────────────────────────────────────────────
        public Doctor createDoctor(Doctor doctor) {
        log.info("Creating new doctor: {}", doctor.getDoctorName());

        // BUSINESS RULE 1: License number must be unique
        boolean licenseExists = doctorDatabase.values().stream()
                .anyMatch(d -> d.getDoctorLicenseNumber().equals(doctor.getDoctorLicenseNumber()));

        if (licenseExists) {
            throw new DuplicateResourceException(
                    "A doctor with license number '" + doctor.getDoctorLicenseNumber() +
                            "' already exists in the system"
            );
        }

        // BUSINESS RULE 2: Consultation fee must be positive
        if (doctor.getDoctorConsultationFee() != null && doctor.getDoctorConsultationFee() < 0) {
            throw new BusinessRuleException(
                    "Consultation fee cannot be negative. Provided: " + doctor.getDoctorConsultationFee()
            );
        }

        // BUSINESS RULE 3: Joining date cannot be in the future
        if (doctor.getJoiningDate() != null &&
                doctor.getJoiningDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException(
                    "Joining date cannot be in the future: " + doctor.getJoiningDate()
            );
        }

        // Assign system-generated ID and set defaults
        doctor.setDoctorId(idSequence.getAndIncrement());
        if (doctor.getDoctorAvailable() == null) {
            doctor.setDoctorAvailable(true);  // New doctors are available by default
        }
        if (doctor.getJoiningDate() == null) {
            doctor.setJoiningDate(LocalDate.now());
        }

        doctorDatabase.put(doctor.getDoctorId(), doctor);
        log.info("✅ Doctor created with ID: {}", doctor.getDoctorId());
        return doctor;
    }

        // ─── FULL UPDATE (PUT) ────────────────────────────────────────────────────
        public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        log.info("Updating doctor with id: {}", id);

        Doctor existing = findById(id);  // Throws 404 if not found

        // BUSINESS RULE: If changing license number, ensure it's still unique
        boolean licenseConflict = doctorDatabase.values().stream()
                .filter(d -> !d.getDoctorId().equals(id))  // Exclude current doctor
                .anyMatch(d -> d.getDoctorLicenseNumber().equals(updatedDoctor.getDoctorLicenseNumber()));

        if (licenseConflict) {
            throw new DuplicateResourceException(
                    "License number '" + updatedDoctor.getDoctorLicenseNumber() +
                            "' is already assigned to another doctor"
            );
        }

        // Preserve the ID and joining date (they shouldn't change)
        updatedDoctor.setDoctorId(id);
        updatedDoctor.setJoiningDate(existing.getJoiningDate());

        doctorDatabase.put(id, updatedDoctor);
        log.info("✅ Doctor {} updated successfully", id);
        return updatedDoctor;
    }

        // ─── PARTIAL UPDATE (PATCH) ───────────────────────────────────────────────
        public Doctor updateAvailability(Long id, Boolean available) {
        log.info("Updating availability for doctor {}: {}", id, available);
        Doctor doctor = findById(id);
        doctor.setDoctorAvailable(available);
        doctorDatabase.put(id, doctor);
        log.info("✅ Doctor {} availability set to: {}", id, available);
        return doctor;
    }

        public Doctor updateFee(Long id, Double newFee) {
        log.info("Updating consultation fee for doctor {}: {}", id, newFee);

        if (newFee < 0) {
            throw new BusinessRuleException(
                    "Consultation fee cannot be negative: " + newFee
            );
        }

        Doctor doctor = findById(id);
        doctor.setDoctorConsultationFee(newFee);
        doctorDatabase.put(id, doctor);
        log.info("✅ Doctor {} fee updated to: {}", id, newFee);
        return doctor;
    }

        // ─── DELETE ───────────────────────────────────────────────────────────────
        public void deleteDoctor(Long id) {
        log.info("Deleting doctor with id: {}", id);
        findById(id);  // Throws 404 if not found
        doctorDatabase.remove(id);
        log.info("✅ Doctor {} deleted", id);
    }

        // ─── UTILITY ──────────────────────────────────────────────────────────────
        public List<String> getAllSpecializations() {
        return doctorDatabase.values().stream()
                .map(Doctor::getDoctorSpecialization)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

        public long countAvailableDoctors() {
        return doctorDatabase.values().stream()
                .filter(d -> Boolean.TRUE.equals(d.getDoctorAvailable()))
                .count();
    }
    }