package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecapPlacesManifeste
{
    private Integer tplc_id;
    private Integer nb_places;
    private Integer nb_hommes;
    private Integer nb_femmes;
    private Integer nb_escale;
    private Integer nb_adultes;
    private Integer nb_total_passagers;
}
