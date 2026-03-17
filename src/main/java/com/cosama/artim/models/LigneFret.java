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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ligneFretId")
public class LigneFret
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ligneFretId;
    private int quantity;
    private float weight;
    private float volume;
    private String details;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name="fretId")
    private Fret fret;

    @ManyToOne
    @JoinColumn(name="tbg_id")
    private TypeBagage typeBagage;

    @ManyToOne
    @JoinColumn(name="cat_id")
    private Categorie categorie;
}
