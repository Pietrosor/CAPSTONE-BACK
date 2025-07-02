package it.epicode.CAPSTONE_BACK.model;

import it.epicode.CAPSTONE_BACK.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "utenti")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "istruttore_id")
    private Utente istruttore;

    @OneToMany(mappedBy = "istruttore")
    private List<Utente> clienti;
}
