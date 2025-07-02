package it.epicode.CAPSTONE_BACK.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public class SchedaDto {
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataCreazione;
    private List<EsercizioDto> esercizi;
}

