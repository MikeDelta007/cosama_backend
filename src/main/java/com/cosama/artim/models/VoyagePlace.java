package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "voyPlcId")
public class VoyagePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long voyPlcId;
    private int vpl_etat;
    private String code_billet;
    //private Boolean in_board;
    @ManyToOne
    @JoinColumn(name="batId")
    private Bateau bateau;
    @ManyToOne
    @JoinColumn(name="plcId")
    private Place place;
    @ManyToOne
    @JoinColumn(name="voyId")
    private Voyage voyage;

}
