package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlanVoyageDTO
{
    private long id;
    private String title;
    private String start;
    private String end;
    private String backgroundColor;
    private long batId;
    private int depart;
    private int arrive;
    private String codeV;
}

