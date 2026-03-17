package com.cosama.artim.repositories;

import java.util.List;

public interface FactureRepositoryCustom {
    List<Object[]> getSituationClient(Long cltcmpt, int annee);
}