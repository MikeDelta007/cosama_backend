package com.cosama.artim.repositories;

import com.cosama.artim.models.TypePiece;
import com.cosama.artim.models.Unite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniteRepository extends JpaRepository<Unite, Long> {

}
