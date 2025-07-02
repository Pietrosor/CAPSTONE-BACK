package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import it.epicode.CAPSTONE_BACK.dto.EsercizioDto;

public interface EsercizioService {

    List<EsercizioDto> searchEsercizi(String keyword);

    EsercizioDto importEsercizio(String esercizioId);
}