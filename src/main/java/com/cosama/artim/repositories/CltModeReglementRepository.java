package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.CltModeReglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CltModeReglementRepository extends JpaRepository<CltModeReglement, Long>
{

}
