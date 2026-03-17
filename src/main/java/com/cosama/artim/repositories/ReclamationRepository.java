package com.cosama.artim.repositories;

import com.cosama.artim.models.Categorie;
import com.cosama.artim.models.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation,Long>
{

}
