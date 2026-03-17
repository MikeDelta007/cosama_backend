package com.cosama.artim.repositories;

import com.cosama.artim.models.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    Ville findVilleByvilId(long vil_id);
}
