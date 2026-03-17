package com.cosama.artim.repositories;

import com.cosama.artim.models.Agence;
import com.cosama.artim.models.Bateau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceRepository extends JpaRepository<Agence,Long> {
}
