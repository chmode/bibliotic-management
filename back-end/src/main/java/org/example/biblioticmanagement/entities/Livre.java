package org.example.biblioticmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String auteur;
    private String description;
    private Float prixParJour;
    private int nombreExemplaires;
    private int exemplairesDisponibles;
    private int statisticNbrEmpruntTotal;

    @ManyToOne
    @JoinColumn(name = "catalogue_id")
    private Catalogue catalogue;

    @OneToMany(mappedBy = "livre")
    @JsonIgnore
    private List<Emprunt> emprunts = new ArrayList<>();


    public Livre(String titre, String auteur, String description, Float prixParJour, int nombreExemplaires, int exemplairesDisponibles, int statisticNbrEmpruntTotal, Catalogue catalogue, List<Emprunt> emprunts) {
        this.titre = titre;
        this.auteur = auteur;
        this.description = description;
        this.prixParJour = prixParJour;
        this.nombreExemplaires = nombreExemplaires;
        this.exemplairesDisponibles = exemplairesDisponibles;
        this.statisticNbrEmpruntTotal = statisticNbrEmpruntTotal;
        this.catalogue = catalogue;
        this.emprunts = emprunts;
    }
    public Livre() {}


    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getDescription() {
        return description;
    }

    public Float getPrixParJour() {
        return prixParJour;
    }

    public int getNombreExemplaires() {
        return nombreExemplaires;
    }

    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }

    public int getStatisticNbrEmpruntTotal() {
        return statisticNbrEmpruntTotal;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrixParJour(Float prixParJour) {
        this.prixParJour = prixParJour;
    }

    public void setNombreExemplaires(int nombreExemplaires) {
        this.nombreExemplaires = nombreExemplaires;
    }

    public void setExemplairesDisponibles(int exemplairesDisponibles) {
        this.exemplairesDisponibles = exemplairesDisponibles;
    }

    public void setStatisticNbrEmpruntTotal(int statisticNbrEmpruntTotal) {
        this.statisticNbrEmpruntTotal = statisticNbrEmpruntTotal;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }
}

