package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEnCompteDTO {
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

    // Les listes peuvent être incluses ou non selon vos besoins
    private List<Long> billets; // ou un DTO spécifique si nécessaire
    private List<Long> frets; // ou un DTO spécifique si nécessaire
    private List<Long> logsSoldes; // ou un DTO spécifique si nécessaire
    private List<Long> cltFactures; // ou un DTO spécifique si nécessaire
    private Long modergltId; // ID de CltModeReglement si nécessaire
}