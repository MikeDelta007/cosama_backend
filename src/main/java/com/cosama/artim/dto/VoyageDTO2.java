package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VoyageDTO2 {
    private long voy_id;
    private int voy_depart;
    private int voy_destination;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voy_datedpt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voy_datearriv;
    private long bat_id;
}
