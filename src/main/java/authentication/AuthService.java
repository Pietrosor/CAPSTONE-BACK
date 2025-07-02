package authentication;

import enumeration.Role;
import lombok.RequiredArgsConstructor;
import model.Utente;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UtenteRepository;

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
