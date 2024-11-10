package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Emprunt;
import org.example.biblioticmanagement.repositories.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    public Emprunt createEmprunt(Emprunt emprunt) {

        return empruntRepository.save(emprunt);
    }

    public boolean deleteEmprunt(Long id) {
        Optional<Emprunt> empruntOptional = empruntRepository.findById(id);
        if (empruntOptional.isPresent()) {
            empruntRepository.delete(empruntOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Emprunt not found
        }
    }

    public List<Emprunt> getAllEmprunts() {

        return empruntRepository.findAll();
    }

    public Emprunt getEmpruntById(Long id) {
        return empruntRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Emprunt updateEmprunt(Long id, Emprunt emprunt) {
        if (empruntRepository.existsById(id)) {
            emprunt.setId(id); // Assuming the Emprunt entity has a setId method
            return empruntRepository.save(emprunt);
        }
        return null; // Return null if not found, or throw an exception
    }

    public Emprunt setEmpruntAsTerminer(Long id) {
        Emprunt emprunt = getEmpruntById(id);
        if (emprunt != null) {
            emprunt.setEmpruntStatus(Emprunt.EmpruntStatus.TERMINE);
            return empruntRepository.save(emprunt);
        }
        return null;
    }

    public Emprunt setEmpruntAsRetard(Long id) {
        Emprunt emprunt = getEmpruntById(id);
        if (emprunt != null) {
            emprunt.setEmpruntStatus(Emprunt.EmpruntStatus.RETARD);
            return empruntRepository.save(emprunt);
        }
        return null;
    }


    public List<Emprunt> historiqueOfAllMyEmprunts(Long userId) {
        List<Emprunt> empruntHestorique = empruntRepository.empruntHistorique(userId);
        return empruntHestorique;
    }

    public List<Emprunt> getActiveEmprunts(Long userId) {
        return empruntRepository.findActiveEmpruntsByUserId(userId);
    }

    public List<Emprunt> getLateEmprunts(Long userId) {
        return empruntRepository.findLateEmpruntsByUserId(userId);
    }

    public List<Emprunt> getTerminatedEmprunts(Long userId) {
        return empruntRepository.findTerminatedEmpruntsByUserId(userId);
    }


}
