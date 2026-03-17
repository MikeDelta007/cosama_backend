package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeBagagesDTO {
    private Long tbgId;
    private String tbgNom;
    private boolean etat;
    private String code;
    private UniteDTO unite;
    private VolumeDTO volume; // Peut être soit VolumeDTO, soit un Long
    private long unite_id;
    private long vol_id;

    // Constructeurs, getters, et setters
}
