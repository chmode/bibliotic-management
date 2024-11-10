package org.example.biblioticmanagement.DataTransferObjectDTO;

import java.time.LocalDate;

public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDate dateEnvoi;
    private boolean lu;
    private Long empruntId;

    public NotificationDTO(Long id, String message, LocalDate dateEnvoi, boolean lu, Long empruntId) {
        this.id = id;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.lu = lu;
        this.empruntId = empruntId;
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

    public void setEmpruntId(Long empruntId) {
        this.empruntId = empruntId;
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

    public Long getEmpruntId() {
        return empruntId;
    }
}

