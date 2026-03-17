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
public class NiveauDTO {
    private long niv_id;
    private String niv_nom;
    private String niv_affiche;
    private List<PlaceDTO> places;
}