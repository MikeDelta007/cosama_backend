package com.cosama.artim.dto;

import com.cosama.artim.models.Bateau;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VoyageDTO {
    private long voy_id;
    private int voy_depart;
    private int voy_destination;
    private String code_voyage;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voy_datedpt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voy_datearriv;
    private int voy_etat;
    @Column(length = 50)
    private String faits_depats;
    @Column(length = 50)
    private String faits_arrives;
    private String observation;
    private String commentaire;
    @Column(length = 50)
    private String debut_embarq;
    @Column(length = 50)
    private String fin_embarq;
    @Column(length = 50)
    private String debut_debarq;
    @Column(length = 50)
    private String fin_debarq;
    @Column(length = 45)
    private String bat_markeur;
    private String motif;

    private long bat_id;

}
