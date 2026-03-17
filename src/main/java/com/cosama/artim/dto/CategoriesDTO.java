package com.cosama.artim.dto;

import com.cosama.artim.models.Age;
import com.cosama.artim.models.OriginPax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDTO {

    private long cat_id;
    private String cat_nom;
    private float cat_prix;
    private float cat_prix_ttc;
    private boolean place;
    private boolean bagage;
    private float cat_remise;
    private Float taux_remise;
    private float cat_forfait;
    private float cat_taxe;
    private float frais_mag;
    private float cat_commission;
    private Age age;
    private OriginPax originPax;
    private TypePlaceDTO typePlace; // DTO si vous souhaitez sérialiser des détails
    private TypeBagageDTO typeBagage; // DTO si vous souhaitez sérialiser des détails
    private List<CritereDTO> criteres; // DTO pour Critere
    private Long tplc_id;
    private Long tbg_id;
    private String code;
}
