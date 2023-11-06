package com.swiftselect.repositories;

import com.swiftselect.domain.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    boolean existsByWorkEmail(String workEmail);
    boolean existsByNotificationEmail(String notificationEmail);
}
