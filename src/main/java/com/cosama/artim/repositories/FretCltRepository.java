package com.cosama.artim.repositories;

import com.cosama.artim.models.FretClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FretCltRepository extends JpaRepository<FretClient, Long> {
}
