package com.cosama.artim.models;

import java.util.List;

public class ResultReport {
    private boolean reportDejaEffectif;
    private List<Critere> criteresBillet;

    public ResultReport(boolean reportDejaEffectif, List<Critere> criteresBillet) {
        this.reportDejaEffectif = reportDejaEffectif;
        this.criteresBillet = criteresBillet;
    }

    public boolean isReportDejaEffectif() {
        return reportDejaEffectif;
    }

    public List<Critere> getCriteresBillet() {
        return criteresBillet;
    }
}