package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneFretDTOS
{
    private long ligneFretId;
    private int quantity;
    private float weight;
    private float volume;
    private String details;
    private Boolean etat;

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
    private float fretMontantHt;
    private boolean applyTVA;
    private boolean applyPayment;
    private String billet;
    private LocalDateTime fretDate;
    private String fretDesc;
    private String usrLogin;
    private LocalDateTime fretPayDate;
    private String fretPayUsr;
    private Boolean fretEtat;
    private float coutMagasinage;
    private float coutMagasinageRemise;
    private String usrMagasinage;
    private LocalDateTime dateMagasinage;
    private String usrLoginPayable;
    private LocalDateTime dateEncaissPayable;
    private long voyId;

    private long fretCltId;
    private String raisonSociale;
    private boolean clientExpEqDest;
    private String clientFirstname;
    private String clientLastname;
    private String numeroPiece;
    private String clientTelephone;
    private String clientEmail;
}
