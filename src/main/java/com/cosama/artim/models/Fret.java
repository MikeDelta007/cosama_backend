package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fretId")
public class Fret
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long fretId;
    private String fretCode;
    private String raisonSocialeDest;
    private String firstnameDest;
    private String lastnameDest;
    private String telephoneDest;
    private String emailDest;
    private boolean expEqDest;
    private float fretAcompte;
    private float fretMontant;
    private float fretTva;
    private float fretRemiseTaux;
    private float fretRemise;
    private float fretMontant_ht;
    private boolean applyTVA;
    private boolean applyPayment;
    private String paymentMethod;
    private String billet;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fretDate;
    private String fretDesc;
    private String usrLogin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fretPayDate;
    private String fretPayUsr;
    private Boolean fretEtat;
    private float coutMagasinage;
    private float coutMagasinageRemise;
    private String usrMagasinage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateMagasinage;
    private String usrLoginPayable;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEncaissPayable;
    private Integer carabane;
    private String motif;

    @ManyToOne
    @JoinColumn(name="cltcmptId")
    @JsonIgnore
    private ClientEnCompte clientEnCompte;

    @ManyToOne
    @JoinColumn(name="voyId")
    @JsonIgnore
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name="fretClientId")
    private FretClient fretClient;

    @OneToMany(mappedBy = "fret", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<LigneFret> ligneFrets;

}
