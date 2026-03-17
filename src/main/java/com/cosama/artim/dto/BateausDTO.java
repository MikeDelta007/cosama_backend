package com.cosama.artim.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BateausDTO {
    private long bat_id;
    private String bat_ref;
    private String bat_nom;
    private String bat_desc;
    private String bat_markeur;
    private int bat_nbplace;
    private boolean bat_etat;
    private long agc_id;

    //private List<PlaceDTO> places;

    private AgenceDTO agence;
}
