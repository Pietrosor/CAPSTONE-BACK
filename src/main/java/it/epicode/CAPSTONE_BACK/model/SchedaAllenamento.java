package it.epicode.CAPSTONE_BACK.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schede_allenamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedaAllenamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(length = 2000)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "istruttore_id", nullable = false)
    private Utente istruttore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Utente cliente;

    @Column(nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "scheda_esercizi",
            joinColumns = @JoinColumn(name = "scheda_id"),
            inverseJoinColumns = @JoinColumn(name = "esercizio_id")
    )
    @Builder.Default
    private Set<Esercizio> esercizi = new HashSet<>();
}
