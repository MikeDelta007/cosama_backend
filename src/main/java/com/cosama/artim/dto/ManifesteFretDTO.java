package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifesteFretDTO
{
    private String fret_code;
    private Float fret_montant_ht;
    private Float fret_montant;
    private Float fret_tva;
    private Date fret_date;
    private Date fret_pay_date;
    private String usr_login;
    private String fret_pay_usr;
    private String firstname_dest;
    private String lastname_dest;
    private String telephone_dest;
    private Float fret_acompte;
    private String raison_social;
    private String contact;
    private String firstname;
    private String lastname;
    private String telephone;
}
