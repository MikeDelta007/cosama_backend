package com.cosama.artim.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AgenceDTO {
    private long agc_id;
    private String agc_nom;
    @Column(length = 10)
    private String sigle;
    @Column(length = 10)
    private String code_agc;
    @Column(length = 20)
    private String agc_tel;
    @Column(length = 20)
    private String agc_fax;
    @Column(length = 150)
    private String agc_resp;
    @Column(length = 150)
    private String agc_mail;

    private long vil_id;

    private List<BateauDTO> bateaus;
    private List<UtilisateurDTO> utilisateurs;


}
