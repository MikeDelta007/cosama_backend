package com.cosama.artim.repositories;

import com.cosama.artim.models.AchatOnLine;
import com.cosama.artim.models.CltFacture;
import com.cosama.artim.models.Fret;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<CltFacture,Long>, FactureRepositoryCustom
{

    @Query(value="SELECT * FROM clt_facture WHERE date_facturation >= :startDate AND date_facturation <= :endDate AND cltcmpt_id = :cltCmptId", nativeQuery = true)
    List<CltFacture> getFactureBeetweenDate(@Param("cltCmptId") Long cltCmptId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
