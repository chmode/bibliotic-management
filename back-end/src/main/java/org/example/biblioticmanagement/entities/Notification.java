package org.example.biblioticmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    @Temporal(TemporalType.DATE)
    private LocalDate dateEnvoi;
    private boolean lu;

    @OneToOne(mappedBy = "notification")
    @JsonIgnore
    private Emprunt emprunt;

    public Notification(String message, LocalDate dateEnvoi, boolean lu, Emprunt emprunt) {
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.lu = lu;
        this.emprunt = emprunt;
    }

    public Notification() {}


    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }

    public Emprunt getEmprunt() {
        return emprunt;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDateEnvoi() {
        return dateEnvoi;
    }

    public boolean isLu() {
        return lu;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateEnvoi(LocalDate dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

}
