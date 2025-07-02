package it.epicode.CAPSTONE_BACK.repository;

import it.epicode.CAPSTONE_BACK.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    List<Utente> findByIstruttoreId(Long istruttoreId);

    boolean existsByUsername(String username);
}
