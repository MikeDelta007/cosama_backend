package com.cosama.artim.repositories;

import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Critere;
import com.cosama.artim.models.GroupeCritere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupeCritereRepository extends JpaRepository<GroupeCritere, Long> {
    GroupeCritere findGroupeCritereBygrpcrtId(long grpcrt_id);

    @Query(value = "SELECT * FROM groupe_critere WHERE on_line = 1", nativeQuery = true)
    List<GroupeCritere> findOnlineGC();
}
