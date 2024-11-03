package org.example.biblioticmanagement.services;

import org.example.biblioticmanagement.entities.Notification;
import org.example.biblioticmanagement.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository; // Ensure you have a corresponding NotificationRepository interface

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public boolean deleteNotification(Long id) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isPresent()) {
            notificationRepository.delete(notificationOptional.get());
            return true; // Deletion successful
        } else {
            return false; // Notification not found
        }
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElse(null); // Return null if not found; consider throwing an exception if preferred
    }

    public Notification updateNotification(Long id, Notification notification) {
        if (notificationRepository.existsById(id)) {
            notification.setId(id); // Assuming the Notification entity has a setId method
            return notificationRepository.save(notification);
        }
        return null; // Return null if not found, or throw an exception
    }

    public Notification setStatusToTrue(Long id) {
        Notification notification = getNotificationById(id);
        if (notification != null) {
            notification.setLu(true);
            notificationRepository.save(notification);
        }
        return notification;
    }


    public List<Notification> getActiveNotificationsByUserId(Long userId) {
        return notificationRepository.getActiveNotificationsByUserId(userId);
    }
}

