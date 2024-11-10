package org.example.biblioticmanagement.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate dateEmprunt;
    @Temporal(TemporalType.DATE)
    private LocalDate dateRetour;
    @Enumerated(EnumType.STRING)
    private EmpruntStatus empruntStatus;

    private Float prixTotal;


    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public enum EmpruntStatus {
        ENCOURS,
        TERMINE,
        RETARD
    }


    public Emprunt(LocalDate dateEmprunt, LocalDate dateRetour, EmpruntStatus empruntStatus, Float prixTotal, Utilisateur utilisateur, Livre livre, Notification notification) {
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.empruntStatus = empruntStatus;
        this.prixTotal = prixTotal;
        this.utilisateur = utilisateur;
        this.livre = livre;
        this.notification = notification;
    }

    public Emprunt(){}



    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Long getId() {
        return id;
    }

    public void setPrixTotal(Float prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Float getPrixTotal() {
        return prixTotal;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public EmpruntStatus getEmpruntStatus() {
        return empruntStatus;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    public void setEmpruntStatus(EmpruntStatus empruntStatus) {
        this.empruntStatus = empruntStatus;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }
}
