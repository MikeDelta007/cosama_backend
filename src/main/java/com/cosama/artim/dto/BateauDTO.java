package com.cosama.artim.dto;

import com.cosama.artim.models.Agence;
import com.cosama.artim.models.Place;
import com.cosama.artim.models.Voyage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Builder
@Data
public class BateauDTO {
    private long bat_id;
    @Column(length = 5)
    private String bat_ref;
    @Column(length = 45)
    private String bat_nom;
    @Column(length = 45)
    private String bat_desc;
    private String bat_markeur;
    private int bat_nbplace;
    private boolean bat_etat;

    private List<VoyageDTO> voyages;
    private List<PlaceDTO> places;

    private long agc_id;
}
