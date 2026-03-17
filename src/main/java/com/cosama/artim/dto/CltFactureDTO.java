package com.cosama.artim.dto;

import com.cosama.artim.models.ClientEnCompte;
import com.cosama.artim.models.CltDetailsFacture;
import com.cosama.artim.models.CltTypeReglement;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CltFactureDTO {
        private long cltfct_id;
        private String libelle;
        private String fact_code;
        private String firstname;
        private String lastname;
        private String telephone;
        private String adresse;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime date_facturation;
        private boolean etat_facturation;
        private boolean est_emis;
        private String usr_facture;
        private String observation;
        private float acompte_facture;
        private float montant_facture;
        private float tva_facture;
        private float montant_verse;
        private float reliquat;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime date_echeance;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime date_paieFacture;
        private String usr_paieFacture;
        private String ref_paieFacture;

        private long clt_tr;

        private long id_cltcmpt;

        private List<CltDetailsFactureDTO> cltDetailsFactures;

    }