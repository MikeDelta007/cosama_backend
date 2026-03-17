package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FretDTOs {
    private long fretId;
    private String fretCode;
    private boolean expEqDest;
    private String raisonSocialeDest;
    private String firstnameDest;
    private String lastnameDest;
    private String telephoneDest;
    private String emailDest;
    private String paymentMethod;
    private float fretAcompte;
    private float fretMontant;
    private float fretTva;
    private float fretRemiseTaux;
    private float fretRemise;
    private float fretMontant_ht;
    private boolean applyTVA;
    private boolean applyPayment;
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
    private long cltcmpt_id;
    private long voy_id;
    private long fretClt_id;
    private FretCltDTO fretCltDTO;
    private long pax_id;
    private Integer carabane;
    private String motif;
    private List<LigneFretDTO> ligneFretDTOList;

}
