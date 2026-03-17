package com.cosama.artim.repositories;

import com.cosama.artim.dto.FretDTOs;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Critere;
import com.cosama.artim.models.Enfant;
import com.cosama.artim.models.Fret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FretRepository extends JpaRepository<Fret,Long>
{
    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.apply_payment = false OR f.fret_acompte > 0 \n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> fretAPayer(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.cltcmpt_id IS NOT NULL\n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> fretCltCompte(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.apply_payment = true \n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> manifesteFret(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND lfr.etat = false AND f.fret_etat = false \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretAnnuler(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.fret_etat = true AND f.apply_payment = false \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretAttente(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.fret_etat = true AND f.apply_payment = true \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretTVA(@Param("voy") Long voyId);

    ///

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.apply_payment = false OR f.fret_acompte > 0 AND f.carabane = 1 \n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> fretAPayerC(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND apply_payment = true AND f.carabane = 1 \n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> manifesteFretC(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND lfr.etat = false AND f.fret_etat = false AND f.carabane = 1 \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretAnnulerC(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone, " +
            "lfr.details, " +
            "lfr.quantity, " +
            "lfr.volume, " +
            "lfr.weight, " +
            "tbg.tbg_nom " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.fret_etat = true AND f.apply_payment = false AND f.carabane = 1 \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretAttenteC(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.fret_etat = true AND f.apply_payment = true AND f.carabane = 1 \n" +
            "ORDER BY f.fret_id DESC",  nativeQuery = true)
    List<Object[]> fretTVAC(@Param("voy") Long voyId);

    @Query(value = "SELECT " +
            "f.fret_code, " +
            "f.fret_montant_ht, " +
            "f.fret_montant, " +
            "f.fret_tva, " +
            "f.fret_date, " +
            "f.fret_pay_date, " +
            "f.usr_login, " +
            "f.fret_pay_usr, " +
            "f.firstname_dest, " +
            "f.lastname_dest, " +
            "f.telephone_dest, " +
            "f.fret_acompte, " +
            "cltc.raison_social, " +
            "cltc.contact, " +
            "fclt.firstname, " +
            "fclt.lastname, " +
            "fclt.telephone " +
            "FROM fret f \n" +
            "LEFT OUTER JOIN client_en_compte cltc ON f.cltcmpt_id = cltc.cltcmpt_id \n" +
            "LEFT OUTER JOIN ligne_fret lfr ON f.fret_id = lfr.fret_id \n" +
            "LEFT OUTER JOIN type_bagage tbg ON lfr.tbg_id = tbg.tbg_id \n" +
            "LEFT OUTER JOIN fret_client fclt ON fclt.fret_clt_id = f.fret_client_id \n" +
            "WHERE f.voy_id = :voy \n" +
            "AND f.cltcmpt_id IS NOT NULL AND f.carabane = 1 \n" +
            "ORDER BY f.fret_date DESC", nativeQuery = true)
    List<Object[]> fretCltCompteC(@Param("voy") Long voyId);

    @Query(value = "SELECT * FROM fret WHERE cltcmpt_id = :cltCmptId", nativeQuery = true)
    List<Fret> findFretByCltCmpt(@Param("cltCmptId") Long cltCmptId);

    @Query(value="SELECT * FROM fret WHERE fret_date >= :startDate AND fret_date <= :endDate AND apply_payment = false AND fret_etat = true AND cltcmpt_id = :cltCmptId", nativeQuery = true)
    List<Fret> getFretByInfos(@Param("cltCmptId") Long cltCmptId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value="SELECT * FROM fret f WHERE f.billet = :billet AND f.apply_payment = false AND f.fret_etat = false", nativeQuery = true)
    Fret getFretByBillet(@Param("billet") String billet);

    @Query(value="SELECT * FROM fret f WHERE f.fret_code = :code AND f.cout_magasinage = 0", nativeQuery = true)
    Fret getFretByCode(@Param("code") String code);

    @Query(value="SELECT * FROM fret f WHERE f.fret_code = :code AND f.apply_payment = true", nativeQuery = true)
    Fret getFretByCode_(@Param("code") String code);
}