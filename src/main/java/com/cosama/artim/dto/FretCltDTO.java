package com.cosama.artim.dto;

import com.cosama.artim.models.Fret;
import com.cosama.artim.models.TypePiece;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FretCltDTO {
    private long fretClt_id;
    private boolean expEqDest;
    private String raisonSociale;
    private String firstname;
    private String lastname;
    private String numeroPiece;
    private String telephone;
    private String email;
    private long tpiece_id;
    private FretDTO fretDTOS;
}
