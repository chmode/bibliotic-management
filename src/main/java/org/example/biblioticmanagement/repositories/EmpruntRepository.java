package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    @Query("select e from Emprunt e where e.utilisateur.id= :userId")
    List<Emprunt> empruntHistorique(@Param("userId") Long id);

    @Query("SELECT e FROM Emprunt e WHERE e.utilisateur.id = :userId AND e.empruntStatus = 'ENCOURS'")
    List<Emprunt> findActiveEmpruntsByUserId(@Param("userId") Long userId);

    @Query("SELECT e FROM Emprunt e WHERE e.utilisateur.id = :userId AND e.empruntStatus = 'RETARD'")
    List<Emprunt> findLateEmpruntsByUserId(@Param("userId") Long userId);

    @Query("SELECT e FROM Emprunt e WHERE e.utilisateur.id = :userId AND e.empruntStatus = 'TERMINE'")
    List<Emprunt> findTerminatedEmpruntsByUserId(@Param("userId") Long userId);
}
