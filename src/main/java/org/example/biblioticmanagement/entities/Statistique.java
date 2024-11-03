package org.example.biblioticmanagement.entities;

import jakarta.persistence.*;


@Entity
public class Statistique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int nombreEmprunts;

    @OneToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;


    public Statistique(String description, int nombreEmprunts, Livre livre, Utilisateur utilisateur) {
        this.description = description;
        this.nombreEmprunts = nombreEmprunts;
        this.livre = livre;
        this.utilisateur = utilisateur;
    }
    public Statistique() {}

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getNombreEmprunts() {
        return nombreEmprunts;
    }

    public Livre getLivre() {
        return livre;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNombreEmprunts(int nombreEmprunts) {
        this.nombreEmprunts = nombreEmprunts;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
