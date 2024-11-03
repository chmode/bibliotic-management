package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {

    Catalogue findByNom(String name);
}

