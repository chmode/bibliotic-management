package org.example.biblioticmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String motDePasse;
    private int nbrEmpruntRetarder;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private int statisticNbrEmpruntTotal;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Emprunt> emprunts = new ArrayList<>();


    public enum UserRole {
        UTILISATEUR,
        BIBLIOTHECAIRE
    }


    public Utilisateur(String nom, String email, String motDePasse, int nbrEmpruntRetarder, int statisticNbrEmpruntTotal, UserRole role, List<Emprunt> emprunts) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.nbrEmpruntRetarder = nbrEmpruntRetarder;
        this.statisticNbrEmpruntTotal = statisticNbrEmpruntTotal;
        this.role = role;
        this.emprunts = emprunts;
    }

    public Utilisateur() {}


    public int getNbrEmpruntRetarder() {
        return nbrEmpruntRetarder;
    }

    public void setNbrEmpruntRetarder(int nbrEmpruntRetarder) {
        this.nbrEmpruntRetarder = nbrEmpruntRetarder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setStatisticNbrEmpruntTotal(int statisticNbrEmpruntTotal) {
        this.statisticNbrEmpruntTotal = statisticNbrEmpruntTotal;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public UserRole getRole() {
        return role;
    }

    public int getStatisticNbrEmpruntTotal() {
        return statisticNbrEmpruntTotal;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

}
