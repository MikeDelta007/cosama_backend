package com.cosama.artim.repositories;

import com.cosama.artim.models.TypeBagage;
import com.cosama.artim.models.TypePiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeBagageRepository extends JpaRepository<TypeBagage, Long> {
    TypeBagage findBagageBytbgId(long tbg_id);

    @Query(value = "SELECT u.unite_nom, COUNT(*) from type_bagage tb\n" +
            "LEFT OUTER JOIN unite u ON tb.unite_id = u.unite_id\n" +
            "GROUP BY u.unite_id", nativeQuery = true)
    List<Object[]> countProductByUnite();


}
