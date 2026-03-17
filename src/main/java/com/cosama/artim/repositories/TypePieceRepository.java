package com.cosama.artim.repositories;

import com.cosama.artim.models.Bateau;
import com.cosama.artim.models.TypePiece;
import com.cosama.artim.models.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePieceRepository extends JpaRepository<TypePiece, Long> {
    //TypePiece findBateauBytpieceId(long tpiece_id);
}
