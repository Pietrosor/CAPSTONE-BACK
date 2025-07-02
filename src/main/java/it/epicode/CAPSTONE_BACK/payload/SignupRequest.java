package it.epicode.CAPSTONE_BACK.payload;


import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String role;
}