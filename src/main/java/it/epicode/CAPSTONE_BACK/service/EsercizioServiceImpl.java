package it.epicode.CAPSTONE_BACK.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import it.epicode.CAPSTONE_BACK.dto.EsercizioDto;
import it.epicode.CAPSTONE_BACK.model.Esercizio;
import it.epicode.CAPSTONE_BACK.repository.EsercizioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EsercizioServiceImpl implements EsercizioService {

    private final EsercizioRepository esercizioRepo;
    private final WebClient webClient = WebClient.create("https://exercisedb-api.vercel.app");

    @Override
    public List<EsercizioDto> searchEsercizi(String keyword) {
        return webClient.get()
                .uri(uri -> uri.path("/exercises").queryParam("name", keyword).build())
                .retrieve()
                .bodyToFlux(EsercizioDto.class)
                .collectList()
                .block();
    }

    @Override
    public EsercizioDto importEsercizio(String esercizioId) {
        EsercizioDto dto = webClient.get()
                .uri("/exercises/{id}", esercizioId)
                .retrieve()
                .bodyToMono(EsercizioDto.class)
                .block();
        esercizioRepo.save(Esercizio.builder()
                .id(dto.getId())
                .name(dto.getName())
                .bodyPart(dto.getBodyPart())
                .equipment(dto.getEquipment())
                .gifUrl(dto.getGifUrl())
                .build());
        return dto;
    }
}
