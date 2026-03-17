package com.cosama.artim.dto;

import com.cosama.artim.models.Age;
import com.cosama.artim.models.OriginPax;
import com.cosama.artim.models.TypeBagage;
import com.cosama.artim.models.TypePlace;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategorieDTO {
    private long cat_id;
    private String cat_nom;
    private float cat_prix;
    private float cat_prix_ttc;
    private boolean place;
    private boolean bagage;
    private float cat_remise;
    private float taux_remise;
    private float cat_forfait;
    private float cat_taxe;
    private float frais_mag;
    private float cat_commission;
    private Age age;
    private OriginPax originPax;

    private Long tplc_id;
    private Long tbg_id;

    private List<CritereDTO> criteres;
    private String code;
}
