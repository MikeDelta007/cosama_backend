package com.cosama.artim.repositories;

import com.cosama.artim.models.Bateau;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BateauRepository extends JpaRepository<Bateau,Long> {
    @Transactional
    @Query("SELECT b FROM Bateau b LEFT JOIN FETCH b.agence")
    List<Bateau> findAllBateauxWithAgence();

}
