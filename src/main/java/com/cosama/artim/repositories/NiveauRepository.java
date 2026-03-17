package com.cosama.artim.repositories;

import com.cosama.artim.models.Niveau;
import com.cosama.artim.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long>
{
    Niveau findBateauBynivId(long niv_id);

    @Query("SELECT n FROM Niveau n JOIN FETCH n.places p JOIN FETCH p.typePlace tp JOIN FETCH p.bateau b WHERE b.batId = :batId")
    List<Niveau> findNiveauWithPlacesByBateauId(@Param("batId") long batId);

    @Query("SELECT n FROM Niveau n " +
            "JOIN FETCH n.places p " +
            "JOIN FETCH p.bateau b " +
            "WHERE b.batId = :batId " +
            "AND p.plcId NOT IN (SELECT vp.place.plcId FROM VoyagePlace vp)")
    List<Niveau> findNiveauWithPlacesNotInVoyagePlaceByBateauId(@Param("batId") long batId);
}