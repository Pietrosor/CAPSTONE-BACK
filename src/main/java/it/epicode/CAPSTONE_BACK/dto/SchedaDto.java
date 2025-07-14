package it.epicode.CAPSTONE_BACK.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = SchedaDto.SchedaDtoBuilder.class)
public class SchedaDto {
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataCreazione;
    private List<EsercizioDto> esercizi;
}

