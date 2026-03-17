package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifesteAdulteDTO {
    private String bilCode;
    private String firstname;
    private String lastname;
    private String numeropiece;
    private String civilite;
    private Long natId;
    private Long typePieceId;
    private Long plcId;
    private Long typePlaceId;
    private Long niveauId;
}