package com.cosama.artim.repositories;

import com.cosama.artim.models.CltModeReglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cosama.artim.models.CltTypeReglement;

@Repository
public interface CltTypeReglementRepository extends JpaRepository<CltTypeReglement, Long>
{

}
