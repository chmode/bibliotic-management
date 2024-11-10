package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Statistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {
}
