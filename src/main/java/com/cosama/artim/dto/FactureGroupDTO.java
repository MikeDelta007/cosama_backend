package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureGroupDTO
{
    private Long cltcmpt_id;
    private String raison_social;
    private List<CltFactureDTO> factures;
}
