package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PassagerWithBilletDTO {
    private long paxId;
    private String numeropiece;
    private String civilite;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateNaiss;
    private String phone;
    private String mail;
    private String adresse;
    private long natId;
    private long tpiece_id;
    private BilletDTO billetsDTOS;
}
