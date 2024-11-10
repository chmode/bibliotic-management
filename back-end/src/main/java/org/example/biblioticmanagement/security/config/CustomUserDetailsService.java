package org.example.biblioticmanagement.security.config;

import org.example.biblioticmanagement.entities.Utilisateur;
import org.example.biblioticmanagement.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by email
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

        // Throw exception if user not found
        if (utilisateur == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert UserRole to GrantedAuthority, adding the ROLE_ prefix
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(utilisateur.getRole().name().split(","))
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());

        // Assuming passwords are stored as plaintext (you should ideally hash them!)
        // If you're using an encoder like BCryptPasswordEncoder, make sure passwords are hashed
        return new User(utilisateur.getEmail(), utilisateur.getMotDePasse(), authorities);
    }
}



