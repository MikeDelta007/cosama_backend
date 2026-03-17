package com.cosama.artim.repositories;

import com.cosama.artim.models.Bateau;
import com.cosama.artim.models.TypeBagage;
import com.cosama.artim.models.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    Voyage findBateauByvoyId(long voy_id);

    @Query("SELECT v.voyId FROM Voyage v WHERE v.bateau.batId = :bateauId AND v.voyEtat = 1 ORDER BY v.voyDatedpt ASC")
    List<Long> findVoyIdByBateauAndEtat(@Param("bateauId") Long bateauId);

    @Query("SELECT v.voyId FROM Voyage v WHERE v.bateau.batId = :bateauId")
    List<Long> findVoyIdByBateau(@Param("bateauId") Long bateauId);

    @Query(value = "SELECT * FROM Voyage v WHERE DATE(v.voy_datedpt) = :date AND v.voy_depart = :depart AND v.bat_id = :bateau", nativeQuery = true)
    Voyage findByDateAndDepart(@Param("date") LocalDate date, @Param("depart") int depart, @Param("bateau") long bat);

    @Query(value = "SELECT v FROM Voyage v WHERE DATE(v.voyDatedpt) >= :date AND v.bateau.id = :batId")
    List<Voyage> findByDate(@Param("date") LocalDate date, @Param("batId") Long batId);

    @Query("SELECT v FROM Voyage v WHERE v.voyDatedpt >= :date AND v.voyEtat = 1")
    List<Voyage> findAllFromDate(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM Voyage v WHERE v.voy_datedpt < CURRENT_DATE AND v.voy_etat = 1", nativeQuery = true)
    List<Voyage> findTheVoyagesBeforeToday();

    @Query(value = "SELECT * FROM Voyage v WHERE v.voy_etat = 1", nativeQuery = true)
    List<Voyage> getVoyageActif();

    Optional<Voyage> findByVoyDepartAndVoyDestinationAndVoyDatedptAndVoyDatearrivAndBateau(
            Long voyDepart,
            Long voyDestination,
            LocalDate voyDatedpt,
            LocalDate voyDatearriv,
            Bateau bateau
    );

    boolean existsByVoyDepartAndVoyDestinationAndVoyDatedptAndVoyDatearrivAndBateauAndVoyIdNot(
            Long voyDepart,
            Long voyDestination,
            LocalDate voyDatedpt,
            LocalDate voyDatearriv,
            Bateau bateau,
            Long voyId
    );
}
