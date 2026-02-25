package org.revature.hospitalmanagementsystemnew.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @Configuration → Marks this class as a source of Spring bean definitions
 * @Slf4j (Lombok) → Injects: private static final Logger log = ...
 * @RequiredArgsConstructor → Generates constructor injection for final fields
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class AppConfig {

    // Spring injects HospitalProperties automatically (it's a @Component)
    private final HospitalProperties hospitalProperties;

    /*
     * CommandLineRunner runs code AFTER Spring context is fully initialized
     * Great for startup validation, logging startup info, etc.
     */
    @Bean
    public CommandLineRunner startupLogger() {
        return args -> {
            log.info("╔═══════════════════════════════════════════════════════╗");
            log.info("║         🏥 HOSPITAL SYSTEM STARTUP COMPLETE           ║");
            log.info("╠═══════════════════════════════════════════════════════╣");
            log.info("║  Name        : {}", hospitalProperties.getHospitalName());
            log.info("║  Address     : {}", hospitalProperties.getHospitalAddress());
            log.info("║  Contact     : {}", hospitalProperties.getHospitalEmergencyContact());
            log.info("║  Max Appt/Day: {}", hospitalProperties.getMaxAppointmentsPerDay());
            log.info("║  SMS Enabled : {}",
                    hospitalProperties.getFeatures().getSmsNotificationEnabled());
            log.info("║  Payments    : {}",
                    hospitalProperties.getFeatures().getOnlinePaymentEnabled());
            log.info("╚═══════════════════════════════════════════════════════╝");
        };
    }
}


