package com.rockstars.musiclibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    
    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}