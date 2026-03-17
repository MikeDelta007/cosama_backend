package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Place;
import com.cosama.artim.models.VoyagePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface VoyagePlaceRepository extends JpaRepository<VoyagePlace, Long>
{
    VoyagePlace findByPlacePlcIdAndVoyageVoyIdAndBateauBatId(Long plcId, Long voyId, Long batId);


    @Query(value = "SELECT * FROM voyage_place WHERE voy_id = :voy AND code_billet = :cb",
            nativeQuery = true)
    VoyagePlace findByVoyageVoyId(@Param("voy") Long voyId, @Param("cb") String cb);

    @Query(value="SELECT COUNT(*) FROM voyage_place WHERE vpl_etat <> 0 AND voy_id = :voyId AND plc_id = :plcId AND bat_id = :batId", nativeQuery = true)
    int countByVoyageAndPlaceAndBateau(@Param("voyId") Long voyId, @Param("plcId") Long plcId, @Param("batId") Long batId);

    @Query(value="SELECT COUNT(b) FROM Billet b WHERE b.bil_etat = true AND b.voy_id = :voyId AND b.plc_id = :plcId AND b.no_show <> 1", nativeQuery = true)
    int countValidBilletsByVoyageAndPlace(@Param("voyId") Long voyId, @Param("plcId") Long plcId);

    @Query(value = "SELECT p.plc_id, CASE WHEN COUNT(vp) > 0 THEN true ELSE false END " +
            "FROM voyage_place vp " +
            "JOIN place p ON vp.plc_id = p.plc_id " +
            "WHERE vp.plc_id IN :placeIds AND vp.voy_id = :voyId " +
            "AND p.tplc_id = :typePlace " +
            "GROUP BY p.plc_id",
            nativeQuery = true)
    List<Object[]> checkPlacesInVoyage(@Param("placeIds") List<Long> placeIds,
                                       @Param("voyId") Long voyId,
                                       @Param("typePlace") Long typePlace);

    @Query(value = "SELECT p.plc_id, CASE WHEN COUNT(vp) > 0 THEN true ELSE false END " +
            "FROM voyage_place vp " +
            "JOIN place p ON vp.plc_id = p.plc_id " +
            "WHERE vp.plc_id IN :placeIds AND vp.voy_id = :voyId " +
            "GROUP BY p.plc_id",
            nativeQuery = true)
    List<Object[]> checkPlacesInVoyage2(@Param("placeIds") List<Long> placeIds,
                                       @Param("voyId") Long voyId);

    @Query(value = "SELECT * FROM voyage_place WHERE voy_id = :voy AND plc_id = :place",
            nativeQuery = true)
    VoyagePlace checkPlace(@Param("voy") Long voyId, @Param("place") Long placeId);

    @Query(value="SELECT \n" +
            "    SUM(CASE WHEN vp.vpl_etat != 2 THEN 1 ELSE 0 END) AS nombre_billets_vendus,\n" +
            "    SUM(CASE WHEN vp.vpl_etat > 2 THEN 1 ELSE 0 END) AS nombre_billets_controles,\n" +
            "    SUM(CASE WHEN vp.vpl_etat = 1 THEN 1 ELSE 0 END) AS en_attente_de_controle\n" +
            "FROM \n" +
            "    voyage_place vp\n" +
            "JOIN \n" +
            "    bateau b ON vp.bat_id = b.bat_id\n" +
            "WHERE\n" +
            "\tvp.voy_id = :voyId", nativeQuery = true)
    List<Object[]> calculateStateBilletOfVoyage(@Param("voyId") Long voyId);


    @Query(value = "SELECT * FROM voyage_place WHERE voy_id = :voyId AND bat_id = :batId AND plc_id = :plcId", nativeQuery = true)
    VoyagePlace findPlaceByVoyAndBat(@Param("voyId") Long voyId, @Param("batId") Long batId, @Param("plcId") Long plcId);


}
