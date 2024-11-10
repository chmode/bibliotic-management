package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Statistique;
import org.example.biblioticmanagement.repositories.StatistiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatistiqueService {

    @Autowired
    private StatistiqueRepository statistiqueRepository; // Ensure you have a corresponding StatistiqueRepository interface

    public Statistique createStatistique(Statistique statistique) {
        return statistiqueRepository.save(statistique);
    }

    public boolean deleteStatistique(Long id) {
        Optional<Statistique> statistiqueOptional = statistiqueRepository.findById(id);
        if (statistiqueOptional.isPresent()) {
            statistiqueRepository.delete(statistiqueOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Statistique not found
        }
    }

    public List<Statistique> getAllStatistiques() {
        return statistiqueRepository.findAll();
    }

    public Statistique getStatistiqueById(Long id) {
        return statistiqueRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Statistique updateStatistique(Long id, Statistique statistique) {
        if (statistiqueRepository.existsById(id)) {
            statistique.setId(id); // Assuming the Statistique entity has a setId method
            return statistiqueRepository.save(statistique);
        }
        return null; // Return null if not found, or throw an exception
    }
}

