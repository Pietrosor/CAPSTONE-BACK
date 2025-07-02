package it.epicode.CAPSTONE_BACK.dto;

import lombok.Data;

@Data
public class EsercizioDto {
    private String id;
    private String name;
    private String bodyPart;
    private String equipment;
    private String gifUrl;

    public EsercizioDto(String id, String name, String bodyPart, String equipment, String gifUrl) {
    }
}

