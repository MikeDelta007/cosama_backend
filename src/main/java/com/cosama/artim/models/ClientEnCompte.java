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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cltcmptId")
public class ClientEnCompte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cltcmptId;
    private String raisonSocial;
    private String firstnameContact;
    private String lastnameContact;
    private String contact;
    private String mail;
    private String cptgen_compta;
    private String cpttiers_compta;
    private float soldeCompte;
    private float plafond;

    @OneToMany(mappedBy = "clientEnCompte", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Billet> billets;

    @OneToMany(mappedBy = "clientEnCompte", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Fret> frets;

    @OneToMany(mappedBy = "clientEnCompte", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<LogsSoldes> logsSoldes;


    @OneToMany(mappedBy = "clientEnCompte", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<CltFacture> cltFactures;

    @ManyToOne
    @JoinColumn(name="modergltId")
    private CltModeReglement cltModeReglement;





}
