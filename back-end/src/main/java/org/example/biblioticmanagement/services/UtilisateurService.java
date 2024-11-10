package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Utilisateur;
import org.example.biblioticmanagement.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Ensure you have a corresponding UtilisateurRepository interface

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {

        return utilisateurRepository.save(utilisateur);
    }

    public boolean deleteUtilisateur(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        if (utilisateurOptional.isPresent()) {
            utilisateurRepository.delete(utilisateurOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Utilisateur not found
        }
    }

    public List<Utilisateur> getAllUtilisateurs() {

        return utilisateurRepository.findAll();
    }

    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
        if (utilisateurRepository.existsById(id)) {
            utilisateur.setId(id); // Assuming the Utilisateur entity has a setId method
            return utilisateurRepository.save(utilisateur);
        }
        return null; // Return null if not found, or throw an exception
    }

    public boolean findByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public Utilisateur login(String email, String password) {
        return utilisateurRepository.findByEmailAndMotDePasse(email, password);
    }

    public  Utilisateur getUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

}
