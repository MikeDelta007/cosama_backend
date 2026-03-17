package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "enfId")
public class Enfant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long enfId;
    private String enfNomComplet;
    private int enfAge;
    @Enumerated(EnumType.STRING)  // Utiliser EnumType.ORDINAL pour stocker l'indice
    private UniteTemps uniteTemps;

    @ManyToOne
    @JoinColumn(name="bilId")
    @JsonIgnore
    private Billet billet;

}
