package com.cosama.artim.dto;

import com.cosama.artim.models.Fret;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LigneFretDTO {
    private long ligneFret_id;
    private int quantity;
    private float weight;
    private float volume;
    private String details;
    private Boolean etat;
    private long fret_id;
    private long tbg_id;
    private long cat_id;
}
