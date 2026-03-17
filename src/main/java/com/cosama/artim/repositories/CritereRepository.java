package com.cosama.artim.repositories;

import com.cosama.artim.models.Bateau;
import com.cosama.artim.models.Critere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CritereRepository extends JpaRepository<Critere,Long> {
    Critere findCritereBycrtId(long crt_id);

    @Query(value="SELECT crt_id FROM critere WHERE crt_nom = :surcl", nativeQuery = true)
    long findIdCriteriaByName(@Param("surcl") String surcl);

}
