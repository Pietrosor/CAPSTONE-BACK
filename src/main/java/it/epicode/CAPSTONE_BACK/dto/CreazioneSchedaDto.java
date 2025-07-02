package it.epicode.CAPSTONE_BACK.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreazioneSchedaDto {
    private String titolo;
    private String descrizione;
    private List<String> eserciziIds;
}

