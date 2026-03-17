package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlaceEditDTO {
    private long plc_id;
    private String plc_code;
    private Boolean plc_etat;
    private String sexe;
    private String situation;
    private Boolean is_carabane;

    private NiveauDTO niveau;
    private TypePlacesDTO typePlace;
    private BateausDTO bateau;

    private long niv_id;
    private long tplc_id;
    private long bat_id;

}
