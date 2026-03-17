package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "voyId")
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long voyId;
    private int voyDepart;
    private int voyDestination;
    private String codeVoyage;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDatedpt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDatearriv;
    private int voyEtat;
    @Column(length = 50)
    private String faitsDepats;
    @Column(length = 50)
    private String faitsArrives;
    @Column(length = 45)
    private String batMarkeur;
    private String observation;
    private String commentaire;
    @Column(length = 50)
    private String debutEmbarq;
    @Column(length = 50)
    private String finEmbarq;
    @Column(length = 50)
    private String debutDebarq;
    @Column(length = 50)
    private String finDebarq;
    private String motif;

    @ManyToOne
    @JoinColumn(name="batId")
    private Bateau bateau;

    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Billet> billets;

    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Fret> frets;

    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<DonneesNavire> donneesNavires;

    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<VoyagePlace> voyagePlaces;



}
