package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UtilisateurDTO {

    private long usr_id;
    @Column(length = 30)
    private String usr_login;
    @Column(length = 100)
    private String usr_password;
    @Column(length = 50)
    private String usr_firstname;
    @Column(length = 50)
    private String usr_lastname;
    @Column(length = 100)
    private String usr_desc;
    private boolean usr_active;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_finvalidite;
    private String usr_reset;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usr_reset_date;
    private boolean sortie;
    private String usr_activ;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usr_activ_date;
    private boolean usr_etat;

    private long agc_id;
    private long prfl_id;
}
