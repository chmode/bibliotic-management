package org.example.biblioticmanagement.controllers;

import org.example.biblioticmanagement.DataTransferObjectDTO.NotificationDTO;
import org.example.biblioticmanagement.entities.Notification;
import org.example.biblioticmanagement.services.NotificationService;
import org.example.biblioticmanagement.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UtilisateurService utilisateurService;

    // Create a new notification
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    // Get all notifications
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Get a notification by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            Long empruntId = notification.getEmprunt() != null ? notification.getEmprunt().getId() : null;
            NotificationDTO notificationDTO = new NotificationDTO(
                    notification.getId(),
                    notification.getMessage(),
                    notification.getDateEnvoi(),
                    notification.isLu(),
                    empruntId
            );
            return new ResponseEntity<>(notificationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Update an existing notification
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.updateNotification(id, notification);
        if (updatedNotification != null) {
            return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a notification by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        boolean deleted = notificationService.deleteNotification(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/settotrue/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<Notification> setLuToTrue(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return new ResponseEntity<>(notificationService.setStatusToTrue(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/activenotifications/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_BIBLIOTHECAIRE', 'SCOPE_UTILISATEUR')")
    public ResponseEntity<List<Notification>> getActiveNotificationsByUserId(@PathVariable Long id) {
        if(utilisateurService.getUtilisateurById(id) != null) {
            return new ResponseEntity<>(notificationService.getActiveNotificationsByUserId(id),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
