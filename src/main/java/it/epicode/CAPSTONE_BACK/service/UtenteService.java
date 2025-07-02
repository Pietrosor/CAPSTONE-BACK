package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import it.epicode.CAPSTONE_BACK.dto.ProfiloUtenteDto;
import it.epicode.CAPSTONE_BACK.dto.ClienteDto;

public interface UtenteService {

    ProfiloUtenteDto getProfilo(String username);


    List<ClienteDto> listaClienti(String istruttoreUsername);

    String findUsernameById(Long id);
}
