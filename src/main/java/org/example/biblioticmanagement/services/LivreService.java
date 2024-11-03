package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Livre;
import org.example.biblioticmanagement.repositories.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LivreService {

    @Autowired
    private LivreRepository livreRepository; // Make sure you have a corresponding LivreRepository interface

    public Livre createLivre(Livre livre) {

        return livreRepository.save(livre);
    }

    public boolean deleteLivre(Long id) {
        Optional<Livre> livreOptional = livreRepository.findById(id);
        if (livreOptional.isPresent()) {
            livreRepository.delete(livreOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Livre not found
        }
    }

    public List<Livre> getAllLivres() {

        return livreRepository.findAll();
    }

    public Livre getLivreById(Long id) {
        return livreRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Livre updateLivre(Long id, Livre livre) {
        if (livreRepository.existsById(id)) {
            livre.setId(id); // Assuming the Livre entity has a setId method
            return livreRepository.save(livre);
        }
        return null; // Return null if not found, or throw an exception
    }


    public List<Livre> getLivresByAuteur(String auteur) {
        return livreRepository.findByAuteur(auteur);
    }

    public List<Livre> getLivresByTitre(String titre) {
        return livreRepository.findByTitre(titre);
    }

    public List<Livre> getLivresByCatalogue(String catalogue) {
        return livreRepository.findByCatalogue(catalogue);
    }
}
