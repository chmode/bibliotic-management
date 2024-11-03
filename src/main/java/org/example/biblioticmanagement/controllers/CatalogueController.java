package org.example.biblioticmanagement.controllers;

import org.example.biblioticmanagement.entities.Catalogue;
import org.example.biblioticmanagement.services.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

    @Autowired
    private CatalogueService catalogueService;

    // Create a new catalogue
    @PostMapping
    public ResponseEntity<Catalogue> createCatalogue(@RequestBody Catalogue catalogue) {
        if (catalogueService.getCatalogueByName(catalogue.getNom()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // or appropriate response
        }
        Catalogue createdCatalogue = catalogueService.createCatalogue(catalogue);
        return new ResponseEntity<>(createdCatalogue, HttpStatus.CREATED);
    }

    // Get all catalogues
    @GetMapping
    public ResponseEntity<List<Catalogue>> getAllCatalogues() {
        List<Catalogue> catalogues = catalogueService.getAllCatalogues();
        return new ResponseEntity<>(catalogues, HttpStatus.OK);
    }

    // Get a catalogue by ID
    @GetMapping("/{id}")
    public ResponseEntity<Catalogue> getCatalogueById(@PathVariable Long id) {
        Catalogue catalogue = catalogueService.getCatalogueById(id);
        if (catalogue != null) {
            return new ResponseEntity<>(catalogue, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a catalogue
    @PutMapping("/{id}")
    public ResponseEntity<Catalogue> updateCatalogue(@PathVariable Long id, @RequestBody Catalogue catalogue) {
        // First, check if the catalogue exists
        Catalogue existingCatalogue = catalogueService.getCatalogueById(id);
        if (existingCatalogue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Catalogue not found
        }

        // Check if the name already exists (excluding the current catalogue)
        Catalogue conflictingCatalogue = catalogueService.getCatalogueByName(catalogue.getNom());
        if (conflictingCatalogue != null && !conflictingCatalogue.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Conflict if name already exists
        }

        // Proceed to update the catalogue
        catalogue.setId(id); // Ensure the ID is set correctly
        Catalogue updatedCatalogue = catalogueService.updateCatalogue(id, catalogue);

        return new ResponseEntity<>(updatedCatalogue, HttpStatus.OK); // Return the updated catalogue
    }


    // Delete a catalogue
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogue(@PathVariable Long id) {
        boolean isDeleted = catalogueService.deleteCatalogue(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
