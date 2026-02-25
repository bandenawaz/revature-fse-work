package com.revature.first_project.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String password;



}
