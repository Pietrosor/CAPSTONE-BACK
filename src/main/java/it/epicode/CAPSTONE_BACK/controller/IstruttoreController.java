package it.epicode.CAPSTONE_BACK.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import it.epicode.CAPSTONE_BACK.dto.*;
import it.epicode.CAPSTONE_BACK.service.*;
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
            @AuthenticationPrincipal UserDetails user
    ) {
        List<ClienteDto> clienti = utenteService.listaClienti(user.getUsername());
        return ResponseEntity.ok(clienti);
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
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long clienteId,
            @RequestBody CreazioneSchedaDto dto
    ) {
        SchedaDto scheda = schedaService.creaScheda(user.getUsername(), clienteId, dto);
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
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long schedaId,
            @PathVariable String esId
    ) {
        esercizioService.importEsercizio(esId);
        SchedaDto scheda = schedaService.aggiungiEsercizioAScheda(
                user.getUsername(), schedaId, esId
        );
        return ResponseEntity.ok(scheda);
    }
}
