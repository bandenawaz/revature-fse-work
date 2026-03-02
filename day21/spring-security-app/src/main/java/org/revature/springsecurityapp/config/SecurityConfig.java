package org.revature.springsecurityapp.config;

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

@Configuration //Tell the spring: this class contains bean definitions
@EnableWebSecurity  //Activates Spring Security's web security support
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        //For everything else just require authentication
                // the fine grained role checks happen at the method level
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails customer = User.builder()
                .username("alice")
                .password(passwordEncoder.encode("alice@123"))
                .roles("CUSTOMER")
                .build();

        UserDetails manager = User.builder()
                .username("bob")
                .password(passwordEncoder.encode("bob@123"))
                .roles("MANAGER")
                .build();

        UserDetails admin = User.builder()
                .username("carol")
                .password(passwordEncoder.encode("carol@123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(customer, manager, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

