package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lignefctId")
public class CltDetailsFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lignefctId;
    private float montant_ligne;
    private float montant_ligne_tva;
    private boolean etat_ligne;
    private String description_ligne;
    private long fretId;

    @ManyToOne
    @JoinColumn(name="cltfctId")
    private CltFacture cltFacture;


}
