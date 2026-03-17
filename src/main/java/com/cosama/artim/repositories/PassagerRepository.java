package com.cosama.artim.repositories;

import com.cosama.artim.dto.PassagerWithBilletDTO;
import com.cosama.artim.models.Passager;
import com.cosama.artim.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassagerRepository extends JpaRepository<Passager, Long> {

    @Query(value="SELECT * FROM Passager WHERE numeropiece = :identifiant", nativeQuery = true)
    Optional<Passager> findByCinOrPassport(@Param("identifiant") String identifiant);
}
