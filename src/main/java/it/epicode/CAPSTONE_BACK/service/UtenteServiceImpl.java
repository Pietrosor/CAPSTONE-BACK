package it.epicode.CAPSTONE_BACK.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import it.epicode.CAPSTONE_BACK.dto.ClienteDto;
import it.epicode.CAPSTONE_BACK.dto.ProfiloUtenteDto;
import it.epicode.CAPSTONE_BACK.model.Utente;
import it.epicode.CAPSTONE_BACK.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepo;

    @Override
    public ProfiloUtenteDto getProfilo(String username) {
        Utente u = utenteRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return ProfiloUtenteDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getRole())
                .build();
    }

    @Override
    public List<ClienteDto> listaClienti(String istruttoreUsername) {
        Utente istr = utenteRepo.findByUsername(istruttoreUsername)
                .orElseThrow(() -> new RuntimeException("Istruttore non trovato"));
        return utenteRepo.findByIstruttoreId(istr.getId())
                .stream()
                .map(c -> new ClienteDto(c.getId(), c.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public String findUsernameById(Long id) {
        return utenteRepo.findById(id)
                .map(Utente::getUsername)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }
}
