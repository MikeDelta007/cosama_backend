package com.cosama.artim.repositories;

import com.cosama.artim.models.Role;
import com.cosama.artim.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    User findByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.profil.id = :profilId")
    List<User> findUsersByProfilId(@Param("profilId") Long profilId);

    @Query(value = "SELECT p.prfl_libelle, COUNT(*) from _user u\n" +
            "LEFT OUTER JOIN profil p ON p.prfl_id = u.prfl_id\n" +
            "GROUP BY p.prfl_id", nativeQuery = true)
    List<Object[]> countProfilByUser();

    @Query(value = "SELECT a.sigle, COUNT(*) from _user u\n" +
            "LEFT OUTER JOIN agence a ON a.agc_id = u.agc_id\n" +
            "GROUP BY a.agc_id", nativeQuery = true)
    List<Object[]> countUserByAgence();

}
