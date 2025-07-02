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
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final UtenteService utenteService;
    private final SchedaService schedaService;

    @GetMapping("/profilo")
    public ResponseEntity<ProfiloUtenteDto> getProfilo(
            @AuthenticationPrincipal UserDetails user
    ) {
        ProfiloUtenteDto profilo = utenteService.getProfilo(user.getUsername());
        return ResponseEntity.ok(profilo);
    }

    @GetMapping("/schede")
    public ResponseEntity<List<SchedaDto>> getSchede(
            @AuthenticationPrincipal UserDetails user
    ) {
        List<SchedaDto> schede = schedaService.getSchedeCliente(user.getUsername());
        return ResponseEntity.ok(schede);
    }
}