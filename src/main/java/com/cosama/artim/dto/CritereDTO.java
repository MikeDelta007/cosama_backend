package com.cosama.artim.dto;

import com.cosama.artim.models.Categorie;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CritereDTO {
    private long crtId;
    //@JsonCreator
    //public CritereDTO(@JsonProperty("crtId") long crtId) {
    //    this.crtId = crtId;
    //}
    //@Column(length = 45)
    private String crt_nom;
    private List<CategorieDTO> categories;
    private List<GroupeCritereDTO> groupeCritereDTOS;

}
