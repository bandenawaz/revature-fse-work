package org.revature.springsecurityapp.service;

import lombok.RequiredArgsConstructor;
import org.revature.springsecurityapp.entity.User;
import org.revature.springsecurityapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //Step 1: Query the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("" +
                        "User not found with username: " + username));

        //Step 2: Convert your User Entity into UserDetails Object
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
//                .disabled(!user.isEnabled())
//                .accountLocked(!user.isAccountNonLocked())
                .build();
    }
}
