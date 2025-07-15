package it.epicode.CAPSTONE_BACK.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import it.epicode.CAPSTONE_BACK.model.Esercizio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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


    @Transactional
    public SchedaDto creaScheda(String istruttoreUser,
                                Long clienteId,
                                CreazioneSchedaDto dto) {
        Utente istr = utenteRepo.findByUsername(istruttoreUser)
                .orElseThrow(() -> new EntityNotFoundException("Istruttore non trovato"));
        Utente client = utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));

        SchedaAllenamento s = new SchedaAllenamento();
        s.setTitolo(dto.getTitolo());
        s.setDescrizione(dto.getDescrizione());
        s.setIstruttore(istr);
        s.setCliente(client);

        if (dto.getEserciziIds() != null) {
            dto.getEserciziIds().forEach(externalId -> {
                Esercizio ex = esercizioRepo.findById(externalId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Esercizio non trovato: " + externalId));
                s.getEsercizi().add(ex);
            });
        }

        schedaRepo.save(s);
        return mapToDto(s);
    }
    @Transactional
    public SchedaDto assegnaSchedaACliente(Long schedaId, Long clienteId) {
        SchedaAllenamento scheda = schedaRepo.findById(schedaId)
                .orElseThrow(() -> new EntityNotFoundException("Scheda non trovata"));
        Utente cliente = utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));

        scheda.setCliente(cliente);
        SchedaAllenamento saved = schedaRepo.save(scheda);
        cliente.getSchede().add(saved);

        return mapToDto(saved);
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
        List<EsercizioDto> lista = s.getEsercizi().stream()
                .map(e -> new EsercizioDto(
                        e.getId(),
                        e.getName(),
                        e.getBodyPart(),
                        e.getEquipment(),
                        e.getGifUrl()))
                .toList();

        return SchedaDto.builder()
                .id(s.getId())
                .titolo(s.getTitolo())
                .descrizione(s.getDescrizione())
                .dataCreazione(s.getDataCreazione())
                .esercizi(lista)
                .build();
    }
    @Override
    public List<SchedaDto> getSchedeClienteById(Long clienteId) {
        utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));
        return schedaRepo
                .findByClienteIdOrderByDataCreazioneDesc(clienteId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

}
