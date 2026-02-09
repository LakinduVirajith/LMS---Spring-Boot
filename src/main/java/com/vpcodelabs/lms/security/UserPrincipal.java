package com.vpcodelabs.lms.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private String id;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
}
