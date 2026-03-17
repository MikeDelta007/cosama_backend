package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SituationClientDTO
{
    private Long cltcmpt_id;
    private String raison_social;
    private Integer annee;
    private String mois;
    private Integer num_mois;
    private Float total_ht;
    private Float total_tva;
    private Float total_ttc;
    private Float total_verse;
    private Float total_reliquat;
}
