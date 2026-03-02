package org.revature.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ← Add this line
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        // For everything else, just require authentication.
                        // The fine-grained role checks happen at the METHOD level.
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails customer = User.builder()
                .username("alice")
                .password(encoder.encode("alice123"))
                .roles("CUSTOMER")
                .build();

        UserDetails manager = User.builder()
                .username("bob")
                .password(encoder.encode("bob123"))
                .roles("MANAGER")
                .build();

        UserDetails admin = User.builder()
                .username("carol")
                .password(encoder.encode("carol123"))
                .roles("ADMIN")
                .build();

        UserDetails auditor = User.builder()
                .username("nawaz")
                .password(encoder.encode("nawaz123"))
                .roles("AUDITOR")
                .build();

        return new InMemoryUserDetailsManager(customer, manager, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}