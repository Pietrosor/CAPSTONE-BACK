package it.epicode.CAPSTONE_BACK.repository;

import it.epicode.CAPSTONE_BACK.model.SchedaAllenamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedaAllenamentoRepository extends JpaRepository<SchedaAllenamento, Long> {

    List<SchedaAllenamento> findByClienteIdOrderByDataCreazioneDesc(Long clienteId);
}