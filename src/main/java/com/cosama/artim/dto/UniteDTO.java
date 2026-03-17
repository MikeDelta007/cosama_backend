package com.cosama.artim.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UniteDTO {
    private long unite_id;
    @Column(length = 150)
    private String unite_nom;
    @Column(length = 50)
    private String unite_code;
    private long vol_id;
    //private List<TypeBagageDTO> typeBagages;
}
