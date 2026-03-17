package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "tbgId")
public class TypeBagage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tbgId;
    @Column(length = 250)
    private String tbgNom;
    private boolean etat;
    private long volId;

    @OneToMany(mappedBy = "typeBagage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Categorie> categories;

    @OneToMany(mappedBy = "typeBagage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<LigneFret> ligneFrets;

    @ManyToOne
    @JoinColumn(name="uniteId")
    private Unite unite;
}
