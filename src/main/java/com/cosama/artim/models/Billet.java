package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bilId")
@Getter
@Setter
public class Billet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bilId;
    private String code_achat;
    private String firstname;
    private String lastname;
    private String numeropiece;
    private String civilite;
    private long nationalite;
    private String bilCode;
    private long batId;
    private long plcId;
    private String ipVente;
    private double bilPht;
    private double bilPtt;
    private double bilTaxe;
    private double bilRemise;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bilDateEmission;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bilDateValidite;

    private Boolean bilEtat;
    private Boolean bilCheck;
    private Boolean bilPrint;
    private int bilPenalite;
    private Long bilReporter;

    private Boolean edit = false;
    private Boolean remb = false;
    private Boolean cancel = false;
    private Boolean rep_sur = false;

    private int noShow;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateNoShow;

    private String userModif;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModif;

    private String userAnnule;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAnnule;

    private String userEmbarq;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEmbarq;

    private String userDebarque;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateDebarque;

    private String userRembours;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateRembours;

    private double mtnRembours;
    private String motifRembours;

    @ManyToOne
    @JoinColumn(name="tpieceId")
    @JsonIgnore
    private TypePiece typePiece;

    @ManyToOne
    @JoinColumn(name="cltcmptId")
    @JsonIgnore
    private ClientEnCompte clientEnCompte;

    @ManyToOne
    @JoinColumn(name="paxId")
    @JsonIgnore
    private Passager passager;

    @ManyToOne
    @JoinColumn(name="tplcId")
    @JsonIgnore
    private TypePlace typePlace;

    @ManyToOne
    @JoinColumn(name="voyId")
    @JsonIgnore
    private Voyage voyage;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "billet_critere",
            joinColumns = @JoinColumn(name = "bilId"),
            inverseJoinColumns = @JoinColumn(name = "crtId")

    )
    private List<Critere> criteres = new ArrayList<>();

    @OneToMany(mappedBy = "billet", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Enfant> enfants;

    @OneToMany(mappedBy = "billet", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<EtatBillet> etatBillets;


}
