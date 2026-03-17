package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilletsDTO {
    private long bilId;
    private String bilCode;
    private String code_achat;
    private String ipVente;
    private String firstname;
    private String lastname;
    private String numeropiece;
    private String civilite;
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
    private Long typePieceId;
    private Long typePlaceId;
    private Long clientEnCompteId;
    private Long passagerId;
    private VoyageDTO voyageDTO;
    private Long batId;
    private Long plcId;
    private Long natId;
    private List<CritereDTO> critereIds;
    private EnfantDTO enfantDTOS;
    private Boolean edit = false;
    private Boolean remb = false;
    private Boolean cancel = false;
    private Boolean rep_sur = false;

}
