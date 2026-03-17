package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, Long>
{

}
