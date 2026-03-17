package com.cosama.artim.repositories;

import com.cosama.artim.models.AchatOnLine;
import com.cosama.artim.models.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchatOnLineRepository extends JpaRepository<AchatOnLine, Long> {
    // Récupère l'achat avec les passagers
    @Query(value = "SELECT * FROM achat_on_line WHERE code_achat = :codeAchat", nativeQuery = true)
    Optional<AchatOnLine> findByCodeAchat(@Param("codeAchat") String codeAchat);
}
