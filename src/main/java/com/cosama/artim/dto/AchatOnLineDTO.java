package com.cosama.artim.dto;

import com.cosama.artim.models.Passager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AchatOnLineDTO {
    private long aolId;
    private long batId;
    private boolean allerSimple;
    private boolean allerRetour;
    private String codeAchat;
    private int voyDepart;
    private Integer voyDestination1;
    private int voyRetour;
    private Integer voyDestination2;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDateDpt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDateRet;
    private int numberPassagers;
    private boolean ifDataDepEqDataRet;
    private double coutAller;
    private double coutRetour;
    private List<PassagerWithBilletDTO> passagerWithBilletDTOS;
}
