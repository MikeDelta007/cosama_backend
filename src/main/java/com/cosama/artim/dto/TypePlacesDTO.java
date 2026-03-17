package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypePlacesDTO {
    private long tplc_id;
    private String tplc_code;
    private String tplc_nom;
}
