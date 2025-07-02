package it.epicode.CAPSTONE_BACK.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
