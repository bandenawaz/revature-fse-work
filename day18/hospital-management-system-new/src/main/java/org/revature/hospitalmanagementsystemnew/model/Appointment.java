package org.revature.hospitalmanagementsystemnew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    private Long appointmentId;
    private Long doctorId;
    private Long patientId;

    private LocalDateTime appointmentDateTime;
    private String appointmentReason;
    private Integer appointmentDurationMinutes;
    private AppointmentStatus appointmentStatus;
    private String appointmentNotes;
    private Double actualFee;
}
