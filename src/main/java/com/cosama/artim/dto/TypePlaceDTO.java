package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TypePlaceDTO {
    private long tplcId;
    private String tplcCode;
    private String tplcNom;
    private float tplcPrix;
    private String batTplcName;
    private byte[] tplcPhoto;

    private List<PlaceDTO> places;
    private List<CategorieDTO> categories;
}