package org.example.biblioticmanagement.controllers;

import org.example.biblioticmanagement.entities.Emprunt;
import org.example.biblioticmanagement.entities.Livre;
import org.example.biblioticmanagement.entities.Notification;
import org.example.biblioticmanagement.entities.Utilisateur;
import org.example.biblioticmanagement.services.EmpruntService;
import org.example.biblioticmanagement.services.LivreService;
import org.example.biblioticmanagement.services.NotificationService;
import org.example.biblioticmanagement.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/emprunt")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private NotificationService notificationService;

    // Create a new emprunt
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Emprunt> createEmprunt(@RequestBody Emprunt emprunt) {
        // Fetch the Livre from the service to ensure it exists and has the required data
        Livre livre = livreService.getLivreById(emprunt.getLivre().getId());
        if (livre == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if Livre not found
        }

        //check exemplaire disponible
        if(livre.getExemplairesDisponibles() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Set the Livre to the Emprunt
        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles()-1);
        livre.setStatisticNbrEmpruntTotal(livre.getStatisticNbrEmpruntTotal()+1);
        emprunt.setLivre(livre);

        // Calculate the number of days between the two dates
        long empruntJourCount = ChronoUnit.DAYS.between(emprunt.getDateEmprunt(), emprunt.getDateRetour());

        // Ensure empruntJourCount is positive to avoid negative duration
        if (empruntJourCount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return 400 for invalid dates
        }

        // Get the price per day from the Livre entity
        Float prixDeLivre = livre.getPrixParJour();
        if (prixDeLivre == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle potential null value
        }

        // Calculate the total price for the emprunt
        emprunt.setPrixTotal(prixDeLivre * empruntJourCount);

        //add 1 to number of emprunt for the user
        Utilisateur user = utilisateurService.getUtilisateurById(emprunt.getUtilisateur().getId());
        if (user != null) {
            user.setStatisticNbrEmpruntTotal(user.getStatisticNbrEmpruntTotal() + 1);
            utilisateurService.updateUtilisateur(user.getId(), user);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle the case where user is null
        }

        //Create Notification
        LocalDate dateToNotification = emprunt.getDateRetour().minusDays(1);
        Notification notification = new Notification("dont forget you bells",dateToNotification,false,emprunt);
        emprunt.setNotification(notification);
        // Create the emprunt
        Emprunt createdEmprunt = empruntService.createEmprunt(emprunt);
        return new ResponseEntity<>(createdEmprunt, HttpStatus.CREATED);
    }





    // Get all emprunts
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Emprunt>> getAllEmprunts() {
        List<Emprunt> emprunts = empruntService.getAllEmprunts();
        return new ResponseEntity<>(emprunts, HttpStatus.OK);
    }

    // Get an emprunt by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Long id) {
        Emprunt emprunt = empruntService.getEmpruntById(id);
        if (emprunt != null) {
            return new ResponseEntity<>(emprunt, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an emprunt
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Emprunt> updateEmprunt(@PathVariable Long id, @RequestBody Emprunt emprunt) {
        // Check if the Emprunt with the given ID exists
        Emprunt existingEmprunt = empruntService.getEmpruntById(id);
        if (existingEmprunt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Float prixDeEmpruntTotal = existingEmprunt.getPrixTotal();

        // Calculate price total if the status is "ENCOURS"
        if (emprunt.getEmpruntStatus() != null && emprunt.getEmpruntStatus().equals(Emprunt.EmpruntStatus.ENCOURS)) {
            // Validate that both dates are not null before calculating
            if (emprunt.getDateEmprunt() != null && emprunt.getDateRetour() != null) {
                long empruntJourCount = ChronoUnit.DAYS.between(emprunt.getDateEmprunt(), emprunt.getDateRetour());
                Livre livre = livreService.getLivreById(emprunt.getLivre().getId());

                // Ensure livre exists to avoid NullPointerException
                if (livre == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or any appropriate status
                }

                prixDeEmpruntTotal = livre.getPrixParJour() * empruntJourCount;
                //emprunt.setPrixTotal(prixDeEmpruntTotal);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Dates cannot be null
            }
        }

        // Update the Emprunt
        emprunt.setPrixTotal(prixDeEmpruntTotal);
        Emprunt updatedEmprunt = empruntService.updateEmprunt(id, emprunt);
        if (updatedEmprunt != null) {
            return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Delete an emprunt
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Void> deleteEmprunt(@PathVariable Long id) {
        boolean isDeleted = empruntService.deleteEmprunt(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/setasterminer/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Emprunt> terminerEmprunt(@PathVariable Long id) {
        Emprunt updatedEmprunt = empruntService.setEmpruntAsTerminer(id);

        if (updatedEmprunt != null) {
            Livre livre = updatedEmprunt.getLivre();
            livre.setExemplairesDisponibles(livre.getExemplairesDisponibles()+1);
            livreService.updateLivre(livre.getId(), livre);
            return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/setasretard/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Emprunt> removeEmprunt(@PathVariable Long id) {
        Emprunt updatedEmprunt = empruntService.setEmpruntAsRetard(id);

        if (updatedEmprunt != null) {
            Utilisateur user = updatedEmprunt.getUtilisateur();
            if (user != null) {
                user.setNbrEmpruntRetarder(user.getNbrEmpruntRetarder() + 1);
                utilisateurService.updateUtilisateur(user.getId(), user);

            }
            return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/historiqueofemprunts/{userId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Emprunt>> getHistoriqueOfEmprunts(@PathVariable Long userId) {
        if(utilisateurService.getUtilisateurById(userId) != null) {
            return new ResponseEntity<>(empruntService.historiqueOfAllMyEmprunts(userId),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/activeemprunts/{userId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Emprunt>> getActiveEmprunts(@PathVariable Long userId) {
        List<Emprunt> activeEmprunts = empruntService.getActiveEmprunts(userId);
        return new ResponseEntity<>(activeEmprunts, HttpStatus.OK);
    }

    // Get late emprunts by user ID
    @GetMapping("/lateemprunts/{userId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Emprunt>> getLateEmprunts(@PathVariable Long userId) {
        List<Emprunt> lateEmprunts = empruntService.getLateEmprunts(userId);
        return new ResponseEntity<>(lateEmprunts, HttpStatus.OK);
    }

    // Get terminated emprunts by user ID
    @GetMapping("/terminatedemprunts/{userId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Emprunt>> getTerminatedEmprunts(@PathVariable Long userId) {
        List<Emprunt> terminatedEmprunts = empruntService.getTerminatedEmprunts(userId);
        return new ResponseEntity<>(terminatedEmprunts, HttpStatus.OK);
    }

}

