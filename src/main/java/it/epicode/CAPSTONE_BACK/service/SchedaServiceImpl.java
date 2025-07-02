package it.epicode.CAPSTONE_BACK.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import it.epicode.CAPSTONE_BACK.dto.CreazioneSchedaDto;
import it.epicode.CAPSTONE_BACK.dto.SchedaDto;
import it.epicode.CAPSTONE_BACK.dto.EsercizioDto;
import it.epicode.CAPSTONE_BACK.model.SchedaAllenamento;
import it.epicode.CAPSTONE_BACK.model.Utente;
import it.epicode.CAPSTONE_BACK.repository.SchedaAllenamentoRepository;
import it.epicode.CAPSTONE_BACK.repository.UtenteRepository;
import it.epicode.CAPSTONE_BACK.repository.EsercizioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedaServiceImpl implements SchedaService {

    private final UtenteRepository utenteRepo;
    private final SchedaAllenamentoRepository schedaRepo;
    private final EsercizioRepository esercizioRepo;

    @Override
    public SchedaDto creaScheda(String istruttoreUser, Long clienteId, CreazioneSchedaDto dto) {
        Utente istr = utenteRepo.findByUsername(istruttoreUser).orElseThrow();
        Utente client = utenteRepo.findById(clienteId).orElseThrow();

        SchedaAllenamento s = SchedaAllenamento.builder()
                .titolo(dto.getTitolo())
                .descrizione(dto.getDescrizione())
                .dataCreazione(LocalDateTime.now())
                .istruttore(istr)
                .cliente(client)
                .build();

        if (dto.getEserciziIds() != null) {
            dto.getEserciziIds().forEach(id -> {
                esercizioRepo.findById(id).ifPresent(s.getEsercizi()::add);
            });
        }

        schedaRepo.save(s);
        return mapToDto(s);
    }

    @Override
    public List<SchedaDto> getSchedeCliente(String clienteUser) {
        Utente client = utenteRepo.findByUsername(clienteUser).orElseThrow();
        return schedaRepo.findByClienteIdOrderByDataCreazioneDesc(client.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SchedaDto aggiungiEsercizioAScheda(String istrUser, Long schedaId, String esId) {
        SchedaAllenamento s = schedaRepo.findById(schedaId).orElseThrow();
        esercizioRepo.findById(esId).ifPresent(s.getEsercizi()::add);
        schedaRepo.save(s);
        return mapToDto(s);
    }

    private SchedaDto mapToDto(SchedaAllenamento s) {
        List<EsercizioDto> es = s.getEsercizi().stream()
                .map(e -> new EsercizioDto(e.getId(), e.getName(), e.getBodyPart(), e.getEquipment(), e.getGifUrl()))
                .collect(Collectors.toList());
        return SchedaDto.builder()
                .id(s.getId())
                .titolo(s.getTitolo())
                .descrizione(s.getDescrizione())
                .dataCreazione(s.getDataCreazione())
                .esercizi(es)
                .build();
    }
}
