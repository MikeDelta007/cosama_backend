package com.cosama.artim.repositories;

import com.cosama.artim.models.Motif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotifRepository extends JpaRepository<Motif,Long>
{

}

