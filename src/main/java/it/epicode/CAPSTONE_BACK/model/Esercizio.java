package it.epicode.CAPSTONE_BACK.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "esercizi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Esercizio {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String bodyPart;

    @Column(nullable = false)
    private String equipment;

    @Column(nullable = false)
    private String gifUrl;

    @ManyToMany(mappedBy = "esercizi")
    @Builder.Default
    private Set<SchedaAllenamento> schede = new HashSet<>();
}