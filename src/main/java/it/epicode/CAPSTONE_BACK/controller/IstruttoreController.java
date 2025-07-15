package it.epicode.CAPSTONE_BACK.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import it.epicode.CAPSTONE_BACK.dto.ClienteDto;
import it.epicode.CAPSTONE_BACK.dto.CreazioneSchedaDto;
import it.epicode.CAPSTONE_BACK.dto.EsercizioDto;
import it.epicode.CAPSTONE_BACK.dto.SchedaDto;
import it.epicode.CAPSTONE_BACK.model.Utente;
import it.epicode.CAPSTONE_BACK.service.UtenteService;
import it.epicode.CAPSTONE_BACK.service.EsercizioService;
import it.epicode.CAPSTONE_BACK.service.SchedaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/istruttore")
@RequiredArgsConstructor
public class IstruttoreController {

    private final UtenteService utenteService;
    private final SchedaService schedaService;
    private final EsercizioService esercizioService;

    @GetMapping("/clienti")
    public ResponseEntity<List<ClienteDto>> listaClienti(
            @AuthenticationPrincipal Utente user
    ) {
        List<ClienteDto> clienti = utenteService.listaClienti(user.getUsername());
        return ResponseEntity.ok(clienti);
    }

    @GetMapping("/clienti/liberi")
    public ResponseEntity<List<ClienteDto>> getClientiLiberi() {
        List<ClienteDto> liberi = utenteService.listaClientiSenzaIstruttore();
        return ResponseEntity.ok(liberi);
    }

    @PostMapping("/clienti/{clienteId}/assegna")
    public ResponseEntity<Void> assegnaCliente(
            @AuthenticationPrincipal Utente user,
            @PathVariable Long clienteId
    ) {
        utenteService.assegnaClienteAlIstruttore(clienteId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clienti/{clienteId}/scheda")
    public ResponseEntity<List<SchedaDto>> getSchedeCliente(
            @PathVariable Long clienteId
    ) {
        String username = utenteService.findUsernameById(clienteId);
        List<SchedaDto> schede = schedaService.getSchedeCliente(username);
        return ResponseEntity.ok(schede);
    }

    @PostMapping("/clienti/{clienteId}/scheda")
    public ResponseEntity<SchedaDto> creaScheda(
            @AuthenticationPrincipal Utente user,
            @PathVariable Long clienteId,
            @RequestBody CreazioneSchedaDto dto
    ) {
        SchedaDto scheda = schedaService.creaScheda(
                user.getUsername(), clienteId, dto
        );
        return ResponseEntity.ok(scheda);
    }

    @GetMapping("/esercizi/search")
    public ResponseEntity<List<EsercizioDto>> cercaEsercizi(
            @RequestParam("q") String keyword
    ) {
        List<EsercizioDto> risultati = esercizioService.searchEsercizi(keyword);
        return ResponseEntity.ok(risultati);
    }

    @PostMapping("/schede/{schedaId}/esercizi/{esId}")
    public ResponseEntity<SchedaDto> aggiungiEsercizio(
            @AuthenticationPrincipal Utente user,
            @PathVariable Long schedaId,
            @PathVariable String esId
    ) {
        esercizioService.importEsercizio(esId);
        SchedaDto scheda = schedaService.aggiungiEsercizioAScheda(
                user.getUsername(), schedaId, esId
        );
        return ResponseEntity.ok(scheda);
    }
    public ResponseEntity<List<SchedaDto>> getSchedePerCliente(
            @PathVariable Long clienteId) {
        List<SchedaDto> schede = schedaService.getSchedeClienteById(clienteId);
        return ResponseEntity.ok(schede);
    }
    @PutMapping("/schede/{schedaId}/cliente/{clienteId}")
    public ResponseEntity<SchedaDto> assegna(
            @PathVariable Long schedaId,
            @PathVariable Long clienteId) {
        SchedaDto dto = schedaService.assegnaSchedaACliente(schedaId, clienteId);
        return ResponseEntity.ok(dto);
    }
}