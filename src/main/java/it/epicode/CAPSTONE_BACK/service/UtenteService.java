package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import it.epicode.CAPSTONE_BACK.dto.ClienteDto;
import it.epicode.CAPSTONE_BACK.dto.ProfiloUtenteDto;

public interface UtenteService {

    ProfiloUtenteDto getProfilo(String username);

    List<ClienteDto> listaClienti(String istruttoreUsername);

    String findUsernameById(Long id);

    List<ClienteDto> listaClientiSenzaIstruttore();

    void assegnaClienteAlIstruttore(Long clienteId, String usernameIstruttore);
}