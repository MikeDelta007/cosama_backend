package com.cosama.artim.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FactureRepositoryCustomImpl implements FactureRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Object[]> getSituationClient(Long cltcmpt, int annee) {
        // Tu peux ignorer SET lc_time ici, ou bien le déplacer côté base de données
        return entityManager.createNativeQuery("""
                WITH mois_annee AS (
                    SELECT generate_series(1, 12) AS num_mois
                ),
                client AS (
                    SELECT cltcmpt_id, raison_social
                    FROM client_en_compte
                    WHERE cltcmpt_id = :cltcmpt_id
                ),
                factures_agg AS (
                    SELECT
                        f.cltcmpt_id,
                        EXTRACT(YEAR FROM f.date_facturation) AS annee,
                        EXTRACT(MONTH FROM f.date_facturation) AS num_mois,
                        SUM(f.montant_facture) AS total_ht,
                        SUM(f.tva_facture) AS total_tva,
                        SUM(f.montant_verse) AS total_verse,
                        SUM(f.reliquat) AS total_reliquat
                    FROM clt_facture f
                    WHERE f.cltcmpt_id = :cltcmpt_id
                      AND EXTRACT(YEAR FROM f.date_facturation) = :annee
                    GROUP BY
                        f.cltcmpt_id,
                        EXTRACT(YEAR FROM f.date_facturation),
                        EXTRACT(MONTH FROM f.date_facturation)
                )
                SELECT
                    c.cltcmpt_id,
                    c.raison_social,
                    :annee AS annee,
                    TO_CHAR(TO_DATE(m.num_mois::text, 'MM'), 'TMMonth') AS mois,
                    m.num_mois,
                    COALESCE(f.total_ht, 0) AS total_ht,
                    COALESCE(f.total_tva, 0) AS total_tva,
                    COALESCE(f.total_ht, 0) + COALESCE(f.total_tva, 0) AS total_ttc,
                    COALESCE(f.total_verse, 0) AS total_verse,
                    COALESCE(f.total_reliquat, 0) AS total_reliquat
                FROM
                    client c
                CROSS JOIN mois_annee m
                LEFT JOIN factures_agg f
                    ON f.cltcmpt_id = c.cltcmpt_id
                    AND f.num_mois = m.num_mois
                ORDER BY
                    m.num_mois;
                """)
                .setParameter("cltcmpt_id", cltcmpt)
                .setParameter("annee", annee)
                .getResultList();
    }
}