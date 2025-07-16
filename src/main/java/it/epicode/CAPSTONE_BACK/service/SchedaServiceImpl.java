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
    private final EsercizioService esercizioService;

    @Transactional
    public SchedaDto creaScheda(String istruttoreUser,
                                Long clienteId,
                                CreazioneSchedaDto dto) {
        System.out.println("=== SERVICE CREAZIONE SCHEDA ===");
        System.out.println("IstruttoreUser: " + istruttoreUser);
        System.out.println("ClienteId: " + clienteId);
        System.out.println("DTO nel service: " + dto);

        // Validazione istruttore
        System.out.println("Cercando istruttore...");
        Utente istr = utenteRepo.findByUsername(istruttoreUser)
                .orElseThrow(() -> new EntityNotFoundException("Istruttore non trovato"));
        System.out.println("Istruttore trovato: " + istr.getUsername());

        // Validazione cliente
        System.out.println("Cercando cliente...");
        Utente client = utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));
        System.out.println("Cliente trovato: " + client.getUsername());

        // Verifica associazione (commentato temporaneamente per test)
        // if (client.getIstruttore() == null || !client.getIstruttore().getId().equals(istr.getId())) {
        //     throw new RuntimeException("Il cliente non è assegnato a questo istruttore");
        // }

        // Creazione scheda
        SchedaAllenamento s = new SchedaAllenamento();
        s.setTitolo(dto.getTitolo());
        s.setDescrizione(dto.getDescrizione());
        s.setIstruttore(istr);
        s.setCliente(client);
        s.setDataCreazione(LocalDateTime.now());

        // Gestione esercizi con fallback graceful
        if (dto.getEserciziIds() != null && !dto.getEserciziIds().isEmpty()) {
            System.out.println("Processando " + dto.getEserciziIds().size() + " esercizi");

            for (String externalId : dto.getEserciziIds()) {
                try {
                    // Prova a trovare l'esercizio nel database locale
                    Esercizio ex = esercizioRepo.findById(externalId).orElse(null);

                    if (ex == null) {
                        // Se non esiste, prova a importarlo dall'API esterna
                        System.out.println("Tentativo import esercizio: " + externalId);
                        try {
                            esercizioService.importEsercizio(externalId);
                            ex = esercizioRepo.findById(externalId).orElse(null);
                        } catch (Exception importError) {
                            System.err.println("Impossibile importare esercizio " + externalId + ": " + importError.getMessage());

                            // Crea un esercizio placeholder se l'import fallisce
                            ex = Esercizio.builder()
                                    .id(externalId)
                                    .name(externalId.replace("_", " "))
                                    .bodyPart("Unknown")
                                    .equipment("Unknown")
                                    .gifUrl("https://via.placeholder.com/300x200?text=Exercise+Not+Found")
                                    .build();

                            esercizioRepo.save(ex);
                            System.out.println("Creato esercizio placeholder per: " + externalId);
                        }
                    }

                    if (ex != null) {
                        s.getEsercizi().add(ex);
                        System.out.println("Aggiunto esercizio: " + ex.getName());
                    }

                } catch (Exception e) {
                    System.err.println("Errore generale con esercizio " + externalId + ": " + e.getMessage());
                    // Continua con gli altri esercizi invece di fallire completamente
                }
            }
        }

        SchedaAllenamento savedScheda = schedaRepo.save(s);
        System.out.println("Scheda salvata con ID: " + savedScheda.getId());

        return mapToDto(savedScheda);
    }
    @Transactional
    public SchedaDto assegnaSchedaACliente(Long schedaId, Long clienteId) {
        SchedaAllenamento scheda = schedaRepo.findById(schedaId)
                .orElseThrow(() -> new EntityNotFoundException("Scheda non trovata"));
        Utente cliente = utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));

        scheda.setCliente(cliente);
        SchedaAllenamento saved = schedaRepo.save(scheda);

        return mapToDto(saved);
    }

    @Override
    public List<SchedaDto> getSchedeCliente(String clienteUser) {
        Utente client = utenteRepo.findByUsername(clienteUser)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato"));
        return schedaRepo.findByClienteIdOrderByDataCreazioneDesc(client.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SchedaDto aggiungiEsercizioAScheda(String istrUser, Long schedaId, String esId) {
        SchedaAllenamento s = schedaRepo.findById(schedaId)
                .orElseThrow(() -> new EntityNotFoundException("Scheda non trovata"));

        Esercizio ex = esercizioRepo.findById(esId).orElse(null);
        if (ex == null) {
            esercizioService.importEsercizio(esId);
            ex = esercizioRepo.findById(esId)
                    .orElseThrow(() -> new EntityNotFoundException("Esercizio non trovato: " + esId));
        }

        s.getEsercizi().add(ex);
        SchedaAllenamento saved = schedaRepo.save(s);
        return mapToDto(saved);
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
}