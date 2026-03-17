package com.cosama.artim.repositories;

import com.cosama.artim.dto.BilletDTO;
import com.cosama.artim.models.AchatOnLine;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Place;
import com.cosama.artim.models.Voyage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Long> {

    @Query(value = "SELECT * FROM billet WHERE voy_id = :voyId", nativeQuery = true)
    List<Billet> findByVoyId(@Param("voyId") Long voyId);

    @Query(value = "SELECT * FROM billet WHERE code_achat = :codeAchat", nativeQuery = true)
    List<Billet> findByCodeAchat(@Param("codeAchat") String codeAchat);

    @Query(value = "SELECT * FROM billet WHERE bil_code = :codeBillet", nativeQuery = true)
    List<Billet> findByCodeBillet(@Param("codeBillet") String codeBillet);

    @Query(value = "SELECT * FROM billet WHERE bil_code = :bilCode", nativeQuery = true)
    Billet findByCodeBil(@Param("bilCode") String bilCode);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " + // Ajout du champ niveau
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code \n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id \n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "AND vp.vpl_etat = 4 AND bc.crt_id = 3 \n" +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> manifesteAdulte(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value="SELECT\n" +
            "    bil.tplc_id,\n" +
            "    COUNT(DISTINCT bil.bil_id) AS nb_places,\n" +
            "    SUM(CASE WHEN bil.civilite = 'M.' \n" +
            "     AND EXISTS (\n" +
            "         SELECT 1 FROM billet_critere bc \n" +
            "         WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 3\n" +
            "     )\n" +
            "     THEN 1 ELSE 0 END\n" +
            ") AS nb_hommes,\n" +
            "    SUM(CASE WHEN bil.civilite IN ('Mme', 'Mlle') \n" +
            "    AND EXISTS (\n" +
            "        SELECT 1 \n" +
            "        FROM billet_critere bc \n" +
            "        WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 3\n" +
            "    )\n" +
            "    THEN 1 ELSE 0 END\n" +
            ") AS nb_femmes,\n" +
            "    SUM(CASE WHEN EXISTS (\n" +
            "        SELECT 1 FROM billet_critere bc WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 19\n" +
            "    ) THEN 1 ELSE 0 END) AS nb_caravanes,\n" +
            "    SUM(CASE WHEN EXISTS (\n" +
            "        SELECT 1 FROM billet_critere bc WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 3\n" +
            "    ) THEN 1 ELSE 0 END) AS nb_adultes,\n" +
            "    COUNT(DISTINCT vp.voy_plc_id) AS nb_total_passagers \n" +
            "FROM billet bil\n" +
            "INNER JOIN voyage_place vp ON vp.code_billet = bil.bil_code AND vp.vpl_etat = 4\n" +
            "WHERE bil.voy_id = :voy\n" +
            "  AND bil.bat_id = :bat\n" +
            "GROUP BY bil.tplc_id\n" +
            "ORDER BY bil.tplc_id", nativeQuery = true)
    List<Object[]> statManifesteAdulte(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " + // Ajout du champ niveau
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id\n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "AND vp.vpl_etat = 4 AND bc.crt_id = 1 \n" +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> manifesteEnfant(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value="SELECT\n" +
            "    bil.tplc_id,\n" +
            "    COUNT(DISTINCT bil.bil_id) AS nb_places,\n" +
            "    SUM(CASE WHEN bil.civilite = 'M.' \n" +
            "     AND EXISTS (\n" +
            "         SELECT 1 FROM billet_critere bc \n" +
            "         WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 1\n" +
            "     )\n" +
            "     THEN 1 ELSE 0 END\n" +
            ") AS nb_hommes,\n" +
            "    SUM(CASE WHEN bil.civilite IN ('Mme', 'Mlle') \n" +
            "    AND EXISTS (\n" +
            "        SELECT 1 FROM billet_critere bc \n" +
            "        WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 1\n" +
            "    )\n" +
            "    THEN 1 ELSE 0 END\n" +
            ") AS nb_femmes,\n" +
            "    SUM(CASE WHEN EXISTS (\n" +
            "        SELECT 1 FROM billet_critere bc WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 19\n" +
            "    ) THEN 1 ELSE 0 END) AS nb_caravanes,\n" +
            "    SUM(CASE WHEN EXISTS (\n" +
            "        SELECT 1 FROM billet_critere bc WHERE bc.bil_id = bil.bil_id AND bc.crt_id = 1\n" +
            "    ) THEN 1 ELSE 0 END) AS nb_adultes,\n" +
            "    COUNT(DISTINCT vp.voy_plc_id) AS nb_total_passagers \n" +
            "FROM billet bil\n" +
            "INNER JOIN voyage_place vp ON vp.code_billet = bil.bil_code AND vp.vpl_etat = 4\n" +
            "WHERE bil.voy_id = :voy\n" +
            "  AND bil.bat_id = :bat\n" +
            "GROUP BY bil.tplc_id\n" +
            "ORDER BY bil.tplc_id", nativeQuery = true)
    List<Object[]> statManifesteEnfant(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " + // Ajout du champ niveau
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "AND vp.vpl_etat <> 4 \n" +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> passagersNonEmbarque(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " +
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "WHERE bil.no_show = 1 AND bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> billetsReportes(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "p.phone, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id" + // Ajout du champ niveau
            " FROM billet bil \n" +
            " LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            " LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            " LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            " LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            " LEFT OUTER JOIN passager p ON p.pax_id = bil.pax_id\n" +
            " WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            " AND vp.vpl_etat = 4 \n" +
            " ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> rapportPAX(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " +
            "FROM billet bil " +
            "LEFT JOIN place plc ON bil.plc_id = plc.plc_id " +
            "LEFT JOIN nationalite nat ON nat.nat_id = bil.nationalite " +
            "LEFT JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id " +
            "LEFT JOIN voyage_place vp ON vp.code_billet = bil.bil_code " +
            "WHERE bil.voy_id = :voy " +
            "AND bil.bat_id = :bat " +
            "AND vp.vpl_etat = 4 " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM billet_critere bc1 " +
            "    WHERE bc1.bil_id = bil.bil_id AND bc1.crt_id = 3 " +
            ") " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM billet_critere bc2 " +
            "    WHERE bc2.bil_id = bil.bil_id AND bc2.crt_id = 19 " +
            ") " +
            "ORDER BY bil.bil_id DESC",
            nativeQuery = true)
    List<Object[]> manifesteAdulteCarab(@Param("voy") Long voyId,
                                        @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " +
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id\n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "AND vp.vpl_etat = 4 " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM billet_critere bc1 " +
            "    WHERE bc1.bil_id = bil.bil_id AND bc1.crt_id = 1 " +
            ") " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM billet_critere bc2 " +
            "    WHERE bc2.bil_id = bil.bil_id AND bc2.crt_id = 19 " +
            ") " +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> manifesteEnfantCarab(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " + // Ajout du champ niveau
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id\n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            "AND vp.vpl_etat <> 4 " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM billet_critere bc1 " +
            "    WHERE bc1.bil_id = bil.bil_id AND bc1.crt_id = 19 " +
            ") " +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> passagersNonEmbarqueCarab(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id " +
            "FROM billet bil \n" +
            "LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            "LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            "LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id \n" +
            "WHERE bil.no_show = 1 AND bil.voy_id = :voy AND bil.bat_id = :bat AND bc.crt_id = 19 \n" +
            "ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> billetsReportesCarab(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "bil.numeropiece, " +
            "bil.civilite, " +
            "p.phone, " +
            "bil.nationalite, " +
            "bil.tpiece_id, " +
            "bil.plc_id, " +
            "bil.tplc_id, " +
            "plc.niv_id" +
            " FROM billet bil \n" +
            " LEFT OUTER JOIN place plc ON bil.plc_id = plc.plc_id \n" +
            " LEFT OUTER JOIN nationalite nat ON nat.nat_id = bil.nationalite \n" +
            " LEFT OUTER JOIN type_piece tpc ON tpc.tpiece_id = bil.tplc_id \n" +
            " LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            " LEFT OUTER JOIN passager p ON p.pax_id = bil.pax_id\n" +
            " LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id\n" +
            " WHERE bil.voy_id = :voy AND bil.bat_id = :bat \n" +
            " AND vp.vpl_etat = 4 AND bc.crt_id = 19 \n" +
            " ORDER BY bil.bil_id DESC", nativeQuery = true)
    List<Object[]> rapportPAXCarab(@Param("voy") Long voyId, @Param("bat") Long batId);

}
