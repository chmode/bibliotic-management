package org.example.biblioticmanagement.controllers;

import org.example.biblioticmanagement.entities.Livre;
import org.example.biblioticmanagement.services.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livre")
public class LivreController {

    @Autowired
    private LivreService livreService;

    // Create a new livre
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Livre> createLivre(@RequestBody Livre livre) {
        Livre createdLivre = livreService.createLivre(livre);
        return new ResponseEntity<>(createdLivre, HttpStatus.CREATED);
    }

    // Get all livres
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Livre>> getAllLivres() {
        List<Livre> livres = livreService.getAllLivres();
        return new ResponseEntity<>(livres, HttpStatus.OK);
    }

    // Get a livre by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Livre> getLivreById(@PathVariable Long id) {
        Livre livre = livreService.getLivreById(id);
        if (livre != null) {
            return new ResponseEntity<>(livre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a livre
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Livre> updateLivre(@PathVariable Long id, @RequestBody Livre livre) {
        Livre updatedLivre = livreService.updateLivre(id, livre);
        if (updatedLivre != null) {
            return new ResponseEntity<>(updatedLivre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a livre
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Void> deleteLivre(@PathVariable Long id) {
        boolean isDeleted = livreService.deleteLivre(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getlivresbyauteur")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Livre>> getLivresByAuteur(@RequestParam String auteur) {
        return new ResponseEntity<>(livreService.getLivresByAuteur(auteur), HttpStatus.OK);
    }

    @GetMapping("/getlivresbytitre")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Livre>> getLivresByTitre(@RequestParam String titre) {
        return new ResponseEntity<>(livreService.getLivresByTitre(titre), HttpStatus.OK);
    }

    @GetMapping("/getlivresbycatalogue")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Livre>> getLivresByCatalogue(@RequestParam String catalogue) {
        return new ResponseEntity<>(livreService.getLivresByCatalogue(catalogue), HttpStatus.OK);
    }
}
