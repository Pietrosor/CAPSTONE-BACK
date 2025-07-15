package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import it.epicode.CAPSTONE_BACK.dto.SchedaDto;
import it.epicode.CAPSTONE_BACK.dto.CreazioneSchedaDto;

public interface SchedaService {

    SchedaDto creaScheda(
            String istruttoreUsername,
            Long clienteId,
            CreazioneSchedaDto dto
    );
    List<SchedaDto> getSchedeCliente(String clienteUsername);

    SchedaDto aggiungiEsercizioAScheda(
            String istruttoreUsername,
            Long schedaId,
            String esercizioId
    );
    List<SchedaDto> getSchedeClienteById(Long clienteId);
}
