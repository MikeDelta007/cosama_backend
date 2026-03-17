package com.cosama.artim.repositories;


import com.cosama.artim.models.CltDetailsFacture;
import com.cosama.artim.models.CltFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineFactureRepository extends JpaRepository<CltDetailsFacture,Long>
{

}
