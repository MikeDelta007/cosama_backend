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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "prflId")
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long prflId;
    private String prflLibelle;
    private boolean addBillet;
    private boolean editBillet;
    private boolean cancelBillet;
    private boolean addCheckBillet;
    private boolean addEmbarqment;
    private boolean doRemboursement;
    private boolean doRepSurclassment;
    private boolean addFret;
    private boolean editFret;
    private boolean cancelFret;
    private boolean delFretDetails;
    private boolean payeFret;
    private boolean checkFret;
    private boolean viewStat;
    private boolean viewEtat;
    private boolean addVoyage;
    private boolean viewVoyage;
    private boolean editVoyage;
    private boolean cancelVoyage;
    private boolean pointerVoyage;
    private boolean planVoyage;
    private boolean editParam;
    private boolean valide;
    private boolean rechercher;
    private boolean addCltCompte;
    private boolean editCltCompte;
    private boolean delCltCompte;
    private boolean addFacture;
    private boolean editFacture;
    private boolean delFacture;
    private boolean addReglement;
    private boolean editReglement;
    private boolean delReglement;
    private boolean bloqPlaces;
    private boolean addPassager;
    private boolean editPassager;
    private boolean delPassager;
    private boolean addNavData;
    private boolean editNavData;
    private boolean edition;
    private boolean actif;
    private boolean reclamation;
    private boolean campagne;

    @OneToMany(mappedBy = "profil", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<User> users;



}
