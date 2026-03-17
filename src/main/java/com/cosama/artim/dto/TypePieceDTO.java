package com.cosama.artim.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TypePieceDTO {
    private long tpiece_id;
    @Column(length = 150)
    private String tpiece_nom;
    private boolean dispo;
}
