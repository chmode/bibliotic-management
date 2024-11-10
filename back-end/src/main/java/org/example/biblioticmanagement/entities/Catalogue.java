package org.example.biblioticmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Catalogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;

    @OneToMany(mappedBy = "catalogue")
    @JsonIgnore
    private List<Livre> livres = new ArrayList<>();


    public Catalogue(String nom, String description, List<Livre> livres) {
        this.nom = nom;
        this.description = description;
        this.livres = livres;
    }
    public Catalogue() {}


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public List<Livre> getLivres() {
        return livres;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }
}
