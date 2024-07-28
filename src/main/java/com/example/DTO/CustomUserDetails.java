package com.example.DTO;

import java.util.List;

public class CustomUserDetails {
    private String username;
    private List<String> roles;

    public CustomUserDetails(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    // getters and setters
}
