package com.cosama.artim.repositories;

import com.cosama.artim.dto.PlaceDTO;
import com.cosama.artim.dto.PlacesBoatDTO;
import com.cosama.artim.models.Passager;
import com.cosama.artim.models.Place;
import com.cosama.artim.models.TypeBagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>
{
    Place findBateauByplcId(long plc_id);

    @Query(value = "SELECT * FROM place p WHERE p.tplc_id = :tplcId AND p.bat_id = :batId AND p.plc_id NOT IN " +
            "(SELECT vp.plc_id FROM voyage_place vp WHERE vp.vpl_etat <> 0 AND vp.voy_id = :voyId AND vp.bat_id = :batId)",
            nativeQuery = true)
    List<Place> findAvailablePlaces(@Param("tplcId") Long tplcId, @Param("voyId") Long voyId, @Param("batId") Long batId);

    @Query(value = "SELECT * FROM place p WHERE p.bat_id = :batId AND p.situation = 'RESERVE'", nativeQuery = true)
    List<Place> placeReserved(@Param("batId") Long batId);

    @Query(value = "SELECT p.* FROM place p " +
            "JOIN voyage_place vp ON p.plc_id = vp.plc_id " +
            "WHERE p.tplc_id = :tplcId " +
            "AND p.bat_id = :batId " +
            "AND vp.vpl_etat = 2 " +
            "AND vp.voy_id = :voyId",
            nativeQuery = true)
    List<Place> findReservedPlaces(@Param("tplcId") Long tplcId, @Param("voyId") Long voyId, @Param("batId") Long batId);


    @Query("SELECT p FROM Place p JOIN FETCH p.typePlace tp JOIN FETCH p.bateau b WHERE b.batId = :batId")
    List<Place> findPlacesByBateauId(@Param("batId") long batId);

    @Query(value="SELECT * FROM Place p WHERE p.bat_id = :batId", nativeQuery = true)
    List<Place> findAllPlaceOfTheBoat(@Param("batId") Long batId);

    @Query(value="SELECT plc_id FROM Place p WHERE p.plc_code = :plcCode", nativeQuery = true)
    Place findPlaceByCode(@Param("plcCode") String plcCode);

    @Query(value ="SELECT \n" +
            "    n.niv_nom AS Niveau,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Chaise' THEN 1 ELSE 0 END) AS CHAISE,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Cabine 2 places' THEN 1 ELSE 0 END) AS C2,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Cabine 4 places' THEN 1 ELSE 0 END) AS C4,\n" +
            "\tSUM(CASE WHEN tp.tplc_nom = 'Cabine 8 places' THEN 1 ELSE 0 END) AS C8\n" +
            "FROM place p\n" +
            "JOIN niveau n ON p.niv_id = n.niv_id\n" +
            "JOIN type_place tp ON p.tplc_id = tp.tplc_id\n" +
            "GROUP BY n.niv_nom\n" +
            "ORDER BY n.niv_nom;", nativeQuery = true)
    List<Object[]> countTypesPlaceByNiveau();

    @Query(value ="SELECT \n" +
            "    b.bat_nom AS Bateau,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Chaise' THEN 1 ELSE 0 END) AS CHAISE,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Cabine 2 places' THEN 1 ELSE 0 END) AS C2,\n" +
            "    SUM(CASE WHEN tp.tplc_nom = 'Cabine 4 places' THEN 1 ELSE 0 END) AS C4,\n" +
            "\tSUM(CASE WHEN tp.tplc_nom = 'Cabine 8 places' THEN 1 ELSE 0 END) AS C8\n" +
            "FROM place p\n" +
            "JOIN bateau b ON p.bat_id = b.bat_id\n" +
            "JOIN type_place tp ON p.tplc_id = tp.tplc_id\n" +
            "GROUP BY b.bat_nom\n" +
            "ORDER BY b.bat_nom;", nativeQuery = true)
    List<Object[]> countTypesPlaceByBateau();

    @Query(value = """
    SELECT 
        b.bat_nom AS bateau,

        COUNT(CASE WHEN tp.tplc_nom = 'Chaise' THEN 1 END) AS chaiseTotal,
        COUNT(CASE WHEN tp.tplc_nom = 'Chaise' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS chaiseOccupee,
        COUNT(CASE WHEN tp.tplc_nom = 'Chaise' THEN 1 END) -
        COUNT(CASE WHEN tp.tplc_nom = 'Chaise' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS chaiseRestante,

        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 2 places' THEN 1 END) AS cabine2Total,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 2 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine2Occupee,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 2 places' THEN 1 END) -
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 2 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine2Restante,

        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 4 places' THEN 1 END) AS cabine4Total,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 4 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine4Occupee,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 4 places' THEN 1 END) -
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 4 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine4Restante,

        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 8 places' THEN 1 END) AS cabine8Total,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 8 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine8Occupee,
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 8 places' THEN 1 END) -
        COUNT(CASE WHEN tp.tplc_nom = 'Cabine 8 places' AND vp.voy_id = :voyageId AND vp.vpl_etat != 0 THEN 1 END) AS cabine8Restante

    FROM place p
    JOIN bateau b ON p.bat_id = b.bat_id
    JOIN type_place tp ON p.tplc_id = tp.tplc_id
    LEFT JOIN voyage_place vp ON vp.plc_id = p.plc_id AND vp.voy_id = :voyageId

    WHERE b.bat_id = :batId
    GROUP BY b.bat_nom
    """, nativeQuery = true)
    List<Object[]> getPlaceStatsByBateauAndVoyage(@Param("batId") Long batId, @Param("voyageId") Long voyageId);


}