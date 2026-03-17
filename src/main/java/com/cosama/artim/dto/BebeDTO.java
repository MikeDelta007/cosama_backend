package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BebeDTO
{
    private String bilCode;
    private String firstname;
    private String lastname;
    private String enfNomComplet;
    private Integer enfAge;
    private String uniteTemps;
    private Long plcId;
}
