package org.revature.springsecurityapp.config;

import lombok.extern.slf4j.Slf4j;
import org.revature.springsecurityapp.entity.User;
import org.revature.springsecurityapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class DataSeeder {

    //CommandLineRunner runs automatically after the app starts
    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            //Only seed if dataabse is empty
            if (userRepository.count() == 0) {
                userRepository.save(
                        new User("alice",
                                passwordEncoder.encode("alice@123"),
                                "ROLE_CUSTOMER")
                );

                userRepository.save(
                        new User(
                                "bob",
                                passwordEncoder.encode("bob@123"),
                                "ROLE_MANAGER"
                        )
                );
                userRepository.save(
                        new User(
                                "carol",
                                passwordEncoder.encode( "carol@123"),
                                "ROLE_ADMIN"
                        )
                );
                log.info("Users seeded into database successfully");
            }
        };

    }
}
