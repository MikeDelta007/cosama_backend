package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Fret;
import com.cosama.artim.models.LigneFret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneFretRepository extends JpaRepository<LigneFret,Long> {
    @Query(value = """
        SELECT 
            lf.ligne_fret_id, lf.quantity, lf.weight, lf.volume, lf.details, lf.etat, 
            f.fret_id, f.fret_code, f.raison_sociale_dest, f.firstname_dest, f.lastname_dest, 
            f.telephone_dest, f.email_dest, f.exp_eq_dest, f.fret_acompte, f.fret_montant, 
            f.fret_tva, f.fret_remise_taux, f.fret_remise, f.fret_montant_ht, f.applytva, 
            f.apply_payment, f.billet, f.fret_date, f.fret_desc, f.usr_login, f.fret_pay_date, 
            f.fret_pay_usr, f.fret_etat, f.cout_magasinage, f.cout_magasinage_remise, 
            f.usr_magasinage, f.date_magasinage, f.usr_login_payable, f.date_encaiss_payable, f.voy_id, 
            fc.fret_clt_id, fc.raison_sociale, fc.exp_eq_dest AS client_exp_eq_dest, 
            fc.firstname AS client_firstname, fc.lastname AS client_lastname, fc.numero_piece, 
            fc.telephone AS client_telephone, fc.email AS client_email
        FROM ligne_fret lf
        JOIN fret f ON lf.fret_id = f.fret_id
        JOIN fret_client fc ON f.fret_client_id = fc.fret_clt_id
        WHERE f.fret_id = :fretId
        """, nativeQuery = true)
    List<Object[]> getLigneFretByFretId(@Param("fretId") long fretId);
}
