package it.epicode.CAPSTONE_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreazioneSchedaDto {
    private String titolo;
    private String descrizione;
    private List<String> eserciziIds;
}

