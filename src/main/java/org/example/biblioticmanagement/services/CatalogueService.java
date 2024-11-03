package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Catalogue;
import org.example.biblioticmanagement.repositories.CatalogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogueService {

    @Autowired
    private CatalogueRepository catalogueRepository;

    public Catalogue createCatalogue(Catalogue catalogue) {

        return catalogueRepository.save(catalogue);
    }

    public boolean deleteCatalogue(Long id) {
        Optional<Catalogue> catalogueOptional = catalogueRepository.findById(id);
        if (catalogueOptional.isPresent()) {
            catalogueRepository.delete(catalogueOptional.get());
            return true;
        } else {
            return false; // Return false if not found
        }
    }

    public List<Catalogue> getAllCatalogues() {

        return catalogueRepository.findAll();
    }

    public Catalogue getCatalogueById(Long id) {
        return catalogueRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Catalogue updateCatalogue(Long id, Catalogue catalogue) {
        if (catalogueRepository.existsById(id)) {
            catalogue.setId(id); // Assuming the Catalogue entity has a setId method
            return catalogueRepository.save(catalogue);
        }
        return null; // Return null if not found, or throw an exception
    }

    public Catalogue getCatalogueByName(String name) {
        return catalogueRepository.findByNom(name);
    }
}

