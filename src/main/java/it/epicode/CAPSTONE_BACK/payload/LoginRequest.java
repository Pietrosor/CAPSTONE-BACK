package it.epicode.CAPSTONE_BACK.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "L'username non può essere vuoto")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    private String password;
}