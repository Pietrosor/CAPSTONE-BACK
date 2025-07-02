package it.epicode.CAPSTONE_BACK.authentication;

import it.epicode.CAPSTONE_BACK.enumeration.Role;
import lombok.RequiredArgsConstructor;
import it.epicode.CAPSTONE_BACK.model.Utente;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import it.epicode.CAPSTONE_BACK.repository.UtenteRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UtenteRepository utenteRepository;
    private final BCryptPasswordEncoder encoder;

    public Utente register(String username, String rawPassword, Role role){
        if(utenteRepository.existsByUsername(username)){
            throw new RuntimeException("Username già in uso");
        }
        Utente u = Utente.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .role(role)
                .build();
        return utenteRepository.save(u);
    }
    public Utente authenticate(String username, String rawPassword) {
        Utente u = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenziali errate"));
        if(!encoder.matches(rawPassword, u.getPassword())){
            throw new RuntimeException("Credenziali errate");
        }
        return u;
    }
    public Utente loadByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

}
