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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "batId")
public class Bateau {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long batId;
    @Column(length = 5)
    private String batRef;
    @Column(length = 45)
    private String batNom;
    @Column(length = 45)
    private String batDesc;
    @Column(length = 45)
    private String batMarkeur;
    private int batNbplace;
    private boolean batEtat;
    private int voyageCourant;
    private int numerotationVoy;

    @OneToMany(mappedBy = "bateau", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Voyage> voyages;

    @OneToMany(mappedBy = "bateau", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Place> places;

    @ManyToOne
    @JoinColumn(name="agcId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    private Agence agence;

    @OneToMany(mappedBy = "bateau", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<VoyagePlace> voyagePlaces;

}
