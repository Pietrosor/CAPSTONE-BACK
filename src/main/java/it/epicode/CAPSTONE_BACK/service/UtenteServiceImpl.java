// src/main/java/it/epicode/CAPSTONE_BACK/service/UtenteServiceImpl.java
package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import java.util.stream.Collectors;

import it.epicode.CAPSTONE_BACK.dto.ClienteDto;
import it.epicode.CAPSTONE_BACK.dto.ProfiloUtenteDto;
import it.epicode.CAPSTONE_BACK.enumeration.Role;
import it.epicode.CAPSTONE_BACK.model.Utente;
import it.epicode.CAPSTONE_BACK.repository.UtenteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepo;

    @Override
    public ProfiloUtenteDto getProfilo(String username) {
        Utente u = utenteRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + username));
        return ProfiloUtenteDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getRole())
                .build();
    }

    @Override
    public List<ClienteDto> listaClienti(String istruttoreUsername) {
        Utente istruttore = utenteRepo.findByUsername(istruttoreUsername)
                .orElseThrow(() -> new RuntimeException("Istruttore non trovato: " + istruttoreUsername));
        return utenteRepo.findClientiByIstruttoreId(istruttore.getId());
    }

    @Override
    public String findUsernameById(Long id) {
        return utenteRepo.findById(id)
                .map(Utente::getUsername)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }

    @Override
    public List<ClienteDto> listaClientiSenzaIstruttore() {
        return utenteRepo
                .findByRoleAndIstruttoreIsNull(Role.CLIENTE)
                .stream()
                .map(this::toClienteDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assegnaClienteAlIstruttore(Long clienteId, String usernameIstruttore) {
        Utente cliente = utenteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato: ID " + clienteId));
        Utente istruttore = utenteRepo.findByUsername(usernameIstruttore)
                .orElseThrow(() -> new EntityNotFoundException("Istruttore non trovato: " + usernameIstruttore));

        cliente.setIstruttore(istruttore);
        utenteRepo.save(cliente);
    }

    private ClienteDto toClienteDto(Utente u) {
        return ClienteDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .build();
    }
}