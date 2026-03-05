package org.revature.studentservice.config;


import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ActuatorSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ── Public: anyone can check health (for load balancers)
                        .requestMatchers(EndpointRequest.to("health", "info"))
                        .permitAll()

                        // ── Sensitive actuator endpoints: only ADMIN role
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .hasRole("ACTUATOR_ADMIN")

                        // ── Your business API: allow all (or add JWT here)
                        .requestMatchers("/api/**")
                        .permitAll()

                        .anyRequest().authenticated()
                );//.httpBasic(auth); // Use Basic Auth for actuator (or JWT in production)

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        // In production: load from DB or OAuth2 provider
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("ops-admin")
                .password("super-secret-ops-password")
                .roles("ACTUATOR_ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}