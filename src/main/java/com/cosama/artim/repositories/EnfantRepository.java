package com.cosama.artim.repositories;

import com.cosama.artim.models.Bateau;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Enfant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnfantRepository extends JpaRepository<Enfant,Long> {

    @Query(value = "SELECT * FROM enfant WHERE bil_id = :bilId", nativeQuery = true)
    Enfant findByBilId(@Param("bilId") Long bilId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "enf.enf_nom_complet, " +
            "enf.enf_age, " +
            "enf.unite_temps, " +
            "bil.plc_id FROM enfant enf \n" +
            "LEFT OUTER JOIN billet bil ON enf.bil_id = bil.bil_id \n" +
            "LEFT OUTER JOIN place plc ON plc.plc_id = bil.plc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat AND vp.vpl_etat = 4\n" +
            "ORDER BY enf.enf_id DESC", nativeQuery = true)
    List<Object[]> manifesteBebe(@Param("voy") Long voyId, @Param("bat") Long batId);

    @Query(value = "SELECT " +
            "bil.bil_code, " +
            "bil.firstname, " +
            "bil.lastname, " +
            "enf.enf_nom_complet, " +
            "enf.enf_age, " +
            "enf.unite_temps, " +
            "bil.plc_id FROM enfant enf \n" +
            "LEFT OUTER JOIN billet bil ON enf.bil_id = bil.bil_id \n" +
            "LEFT OUTER JOIN place plc ON plc.plc_id = bil.plc_id \n" +
            "LEFT OUTER JOIN voyage_place vp ON vp.code_billet = bil.bil_code\n" +
            "LEFT OUTER JOIN billet_critere bc ON bc.bil_id = bil.bil_id\n"+
            "WHERE bil.voy_id = :voy AND bil.bat_id = :bat AND bc.crt_id = 19 AND vp.vpl_etat = 4\n" +
            "ORDER BY enf.enf_id DESC", nativeQuery = true)
    List<Object[]> manifesteBebeCarab(@Param("voy") Long voyId, @Param("bat") Long batId);

}
