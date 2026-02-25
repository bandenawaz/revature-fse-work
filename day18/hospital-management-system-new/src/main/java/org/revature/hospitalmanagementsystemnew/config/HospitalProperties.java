package org.revature.hospitalmanagementsystemnew.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hospital")
@Data
public class HospitalProperties {

    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhoneNumber;
    private Integer maxAppointmentsPerDay;
    private Integer appointmentDurationMinutes;
    private String hospitalEmergencyContact;
    private Features features;

    @Data
    public static class Features {
        private Boolean smsNotificationEnabled;
        private Boolean emailNotificationEnabled;
        private Boolean onlinePaymentEnabled;
    }

}
