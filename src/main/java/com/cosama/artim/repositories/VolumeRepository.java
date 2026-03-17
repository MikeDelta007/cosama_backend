package com.cosama.artim.repositories;

import com.cosama.artim.models.Ville;
import com.cosama.artim.models.Volume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Long> {
}
