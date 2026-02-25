package org.revature.hospitalmanagementsystemnew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Doctor {

    Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private String doctorLicenseNumber;
    private String doctorEmail;
    private String doctorPhoneNumber;
    private Double doctorConsultationFee;
    private LocalDate joiningDate;
    private Boolean doctorAvailable;
    private List<String> doctorQualifications;

}
