package com.cosama.artim.dto;

import com.cosama.artim.models.Categorie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TypeBagageDTO {
    private long tbg_id;
    @Column(length = 250)
    private String tbg_nom;
    private boolean etat;
    private String code;

    private long unite_id;
    private long vol_id;
    private List<CategorieDTO> categories;

    private UniteDTO unite;
    private VolumeDTO volume; // Peut être soit VolumeDTO, soit un Long
}
