package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilRepository extends JpaRepository<Profil,Long> {
    @Query(value = "SELECT * FROM profil WHERE actif = true", nativeQuery = true)
    List<Profil> getAllProfil();
}