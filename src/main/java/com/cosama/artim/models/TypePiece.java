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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "tpieceId")
public class TypePiece {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tpieceId;
    private String tpieceNom;
    private Boolean dispo = true;

    @OneToMany(mappedBy = "typePiece", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Billet> billets;

    @OneToMany(mappedBy = "typePiece", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<FretClient> fretClients;

    @OneToMany(mappedBy = "typePiece", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Passager> passagers;
}
