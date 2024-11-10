package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    @Query("select l from Livre l where replace(lower(trim(l.auteur)), ' ', '') = replace(lower(trim(:auteur)), ' ', '')")
    List<Livre> findByAuteur(@Param("auteur") String auteur);

    @Query("select l from Livre l where replace(lower(trim(l.titre)), ' ', '') = replace(lower(trim(:titre)), ' ', '')")
    List<Livre> findByTitre(@Param("titre") String titre);

    @Query("select l from Livre l where replace(lower(trim(l.catalogue.nom)), ' ', '') = replace(lower(trim(:catalogue)), ' ', '')")
    List<Livre> findByCatalogue(@Param("catalogue") String catalogue);
}
