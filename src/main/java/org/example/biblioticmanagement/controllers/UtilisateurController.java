package org.example.biblioticmanagement.controllers;

import org.example.biblioticmanagement.entities.Utilisateur;
import org.example.biblioticmanagement.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/save")
    public ResponseEntity<Utilisateur> save(@RequestBody Utilisateur utilisateur) {
        if (utilisateurService.findByEmail(utilisateur.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // or appropriate response
        }
        utilisateur.setNbrEmpruntRetarder(0);
        utilisateur.setStatisticNbrEmpruntTotal(0);
        Utilisateur savedUtilisateur = utilisateurService.createUtilisateur(utilisateur);
        return new ResponseEntity<>(savedUtilisateur, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = utilisateurService.deleteUtilisateur(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Utilisateur>> findAll() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        // Check if the user exists
        Utilisateur existingUser = utilisateurService.getUtilisateurById(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Check for email conflict
        Utilisateur conflictUser = utilisateurService.getUserByEmail(utilisateur.getEmail());
        if (conflictUser != null && !conflictUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Return conflict status
        }

        // Proceed to update the user
        utilisateur.setId(id); // Set the ID to ensure the correct user is updated
        Utilisateur updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateur);

        return new ResponseEntity<>(updatedUtilisateur, HttpStatus.OK); // Return the updated user
    }


    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur != null) {
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Utilisateur utilisateur = utilisateurService.login(email, password);
        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        }
    }

}

