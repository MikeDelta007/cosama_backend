package com.cosama.artim.dto;

import com.cosama.artim.models.CltFacture;
import com.cosama.artim.models.Type_Bil_Fret;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CltDetailsFactureDTO
{
    private long lignefct_id;
    private float montant_ligne;
    private float montant_ligne_tva;
    private boolean etat_ligne;
    private String description_ligne;
    private long fret_id;

    private long cltfact_id;
}
