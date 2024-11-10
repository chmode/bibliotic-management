package org.example.biblioticmanagement.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.example.biblioticmanagement.entities.Utilisateur;
import org.example.biblioticmanagement.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
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
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = utilisateurService.deleteUtilisateur(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
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
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur != null) {
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


   /*@PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Utilisateur utilisateur = utilisateurService.login(email, password);
        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        }
    }*/

   @GetMapping("/all")
   @PreAuthorize("hasAuthority('SCOPE_BIBLIOTHECAIRE')")
   public ResponseEntity<List<Utilisateur>> findAll() {
       List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
       return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
   }
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  JwtEncoder jwtEncoder;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        // Authenticate the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Create JWT Token after successful authentication
        Instant instant = Instant.now();

        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());


        Utilisateur utilisateur = utilisateurService.getUserByEmail(email);
        Long userId = utilisateur.getId();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(instant)
                .expiresAt(instant.plus(100, ChronoUnit.DAYS))
                .claim("scope", roles)  // Updated claim name from 'role' to 'roles' for clarity
                .claim("id", userId)
                .build();


        JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSet
                );
        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        Map<String, String> response = Map.of("token", jwt);
        return ResponseEntity.ok(response);  // Return response with 200 status
    }


}


