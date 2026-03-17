package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportPAX
{
    private String bilCode;
    private String firstname;
    private String lastname;
    private String numeropiece;
    private String civilite;
    private String phone;
    private Long typePieceId;
    private Long typePlaceId;
    private Long plcId;
    private Long natId;
    private Integer nivId;
}
