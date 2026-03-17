package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VilleDTO {
    private long vil_id;
    @Column(length = 10)
    private String vil_code;
    @Column(length = 150)
    private String vil_nom;

    @JsonIgnore
    private List<AgenceDTO> agences;
}
