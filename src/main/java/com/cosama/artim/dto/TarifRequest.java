package com.cosama.artim.dto;

import com.cosama.artim.models.Critere;

import java.util.List;

public class TarifRequest {
    private Long tplId;
    private List<Critere> criteres;

    // Getters et setters

    public Long getTplId() {
        return tplId;
    }

    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

    public List<Critere> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<Critere> criteres) {
        this.criteres = criteres;
    }
}