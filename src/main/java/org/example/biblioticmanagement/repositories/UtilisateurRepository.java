package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);

    Utilisateur findByEmailAndMotDePasse(String email, String motDePasse);

    Utilisateur findByEmail(String email);


}
