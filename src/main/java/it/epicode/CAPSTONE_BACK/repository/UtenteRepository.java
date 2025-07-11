package it.epicode.CAPSTONE_BACK.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.epicode.CAPSTONE_BACK.model.Utente;
import it.epicode.CAPSTONE_BACK.dto.ClienteDto;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    
    Optional<Utente> findByUsername(String username);

    boolean existsByUsername(String username);

    // 3) La query per caricare i clienti
    @Query("""
      SELECT new it.epicode.CAPSTONE_BACK.dto.ClienteDto(
        u.id,
        u.username
      )
      FROM Utente u
      WHERE u.istruttore.id = :istrId
    """)
    List<ClienteDto> findClientiByIstruttoreId(@Param("istrId") Long istruttoreId);
}