package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "modergltId")
public class CltModeReglement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long modergltId;
    private String libelleModeReglm;
    private int dureeJr;
    private boolean actif;

    @OneToMany(mappedBy = "cltModeReglement", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<ClientEnCompte> clientEnComptes;



}
