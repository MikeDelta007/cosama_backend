package com.cosama.artim.repositories;

import com.cosama.artim.dto.CategoriesDTO;
import com.cosama.artim.models.Categorie;
import com.cosama.artim.models.Critere;
import com.cosama.artim.models.TypeBagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    Categorie findCategorieBycatId(long cat_id);

    @Query("SELECT c FROM Categorie c WHERE c.typeBagage.tbgId = :tbgId")
    Categorie findCategorieByTbgId(@Param("tbgId") Long tbgId);

    @Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.typePlace LEFT JOIN FETCH c.typeBagage LEFT JOIN FETCH c.criteres")
    List<Categorie> findAllWithDetails();

    @Query(value = "SELECT * FROM categorie c WHERE c.code = 'V'", nativeQuery = true)
    List<Categorie> findByFixedCode();

}
