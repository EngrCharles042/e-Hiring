package com.swiftselect.repositories;

import com.swiftselect.domain.entities.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
