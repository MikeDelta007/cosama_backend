package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "catId")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long catId;
    private String catNom;
    private float catPrix;
    private float catPrixTtc;
    private boolean place;
    private boolean bagage;
    private Float tauxRemise;
    private float catRemise;
    private float catForfait;
    private float catTaxe;
    private float fraisMag;
    private float catCommission;
    @Enumerated(EnumType.STRING)  // Utiliser EnumType.ORDINAL pour stocker l'indice
    private Age age;
    private String code;

    @Enumerated(EnumType.STRING)  // Utiliser EnumType.ORDINAL pour stocker l'indice
    private OriginPax originPax;

    @ManyToOne
    @JoinColumn(name="tplc_id", nullable = true)
    @JsonManagedReference
    private TypePlace typePlace;

    @ManyToOne
    @JoinColumn(name="tbg_id", nullable = true)
    @JsonManagedReference
    private TypeBagage typeBagage;

    @ManyToMany
    @JoinTable(
            name = "categorie_critere",
            joinColumns = @JoinColumn(name = "catId"),
            inverseJoinColumns = @JoinColumn(name = "crtId")
    )
    private List<Critere> criteres;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<LigneFret> ligneFrets;

}
