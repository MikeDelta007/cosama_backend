package com.cosama.artim.repositories;

import com.cosama.artim.models.Place;
import com.cosama.artim.models.TypePiece;
import com.cosama.artim.models.TypePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypePlaceRepository extends JpaRepository<TypePlace, Long> {
    TypePlace findBateauBytplcId(long tplc_id);

    @Query(value="SELECT tplc_code FROM type_place WHERE tplc_id = :tplcId", nativeQuery = true)
    String findNameTypePlace(@Param("tplcId") Long tplcId);

}

