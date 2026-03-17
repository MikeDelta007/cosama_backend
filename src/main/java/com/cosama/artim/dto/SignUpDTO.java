package com.cosama.artim.dto;

import lombok.Data;

@Data
public class SignUpDTO {
    private String usr_firstname;
    private String usr_lastname;
    private String usr_login;
    private String usr_password;
    private String usr_description;
    private Long agc_id;
    private Long prfl_id;
    private boolean etat;
}
