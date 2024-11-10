package org.example.biblioticmanagement.repositories;

import org.example.biblioticmanagement.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.lu = false AND n.emprunt.utilisateur.id = :userId")
    List<Notification> getActiveNotificationsByUserId(@Param("userId") Long userId);
}
