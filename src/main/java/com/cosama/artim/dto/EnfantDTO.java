package com.cosama.artim.dto;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.UniteTemps;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnfantDTO {
    private String enfNomComplet;
    private int enfAge;
    @Enumerated(EnumType.STRING)
    private UniteTemps uniteTemps;
    private long bil_id;
}
