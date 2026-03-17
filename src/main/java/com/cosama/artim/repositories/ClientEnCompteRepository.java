package com.cosama.artim.repositories;

import com.cosama.artim.models.ClientEnCompte;
import com.cosama.artim.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientEnCompteRepository extends JpaRepository<ClientEnCompte, Long> {
    @Query(value="SELECT * FROM client_en_compte WHERE cltcmpt_id = :cltC", nativeQuery = true)
    ClientEnCompte findCltEnCompteById(@Param("cltC") Long cltC);
}
