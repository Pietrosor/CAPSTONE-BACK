package it.epicode.CAPSTONE_BACK.dto;

import it.epicode.CAPSTONE_BACK.enumeration.Role;
import lombok.Builder;

@Builder
public class ProfiloUtenteDto {
    private Long id;
    private String username;
    private Role role;
    // campi aggiuntivi (es. nome, email, peso, età…)
}

