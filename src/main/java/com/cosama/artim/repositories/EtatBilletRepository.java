package com.cosama.artim.repositories;

import com.cosama.artim.dto.EtatBilletDTO;
import com.cosama.artim.models.Agence;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.EtatBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtatBilletRepository extends JpaRepository<EtatBillet,Long> {

    @Query(value = "SELECT eb_id, bil_time, color, icon, status, bil_id FROM etat_billet WHERE bil_id = :bilId", nativeQuery = true)
    List<EtatBillet> findByCodeBil(@Param("bilId") Long bilId);
}
