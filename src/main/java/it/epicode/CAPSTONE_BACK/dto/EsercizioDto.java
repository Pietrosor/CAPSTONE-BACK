package it.epicode.CAPSTONE_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsercizioDto {
    private String id;
    private String name;
    private String bodyPart;
    private String equipment;
    private String gifUrl;

}

