package it.epicode.CAPSTONE_BACK.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "L'username non può essere vuoto")
    @Size(min = 3, max = 20, message = "L'username deve avere tra 3 e 20 caratteri")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Il ruolo non può essere vuoto")
    @Pattern(regexp = "CLIENTE|ISTRUTTORE", message = "Il ruolo può essere solo CLIENTE o ISTRUTTORE")
    private String role;
}