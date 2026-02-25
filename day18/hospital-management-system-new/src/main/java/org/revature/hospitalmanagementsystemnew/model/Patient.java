package org.revature.hospitalmanagementsystemnew.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientAddress;
    private String patientEmergencyContact;
    private LocalDate patientDateOfBirth;
    private String patientGender;
    private String patientBloodGroup;

    @ToString.Exclude
    private String patientMedicalHistory;

}
