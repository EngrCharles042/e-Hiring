package com.swiftselect.repositories;

import com.swiftselect.domain.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByWorkEmail(String workEmail);
    boolean existsByWorkEmail(String workEmail);
    boolean existsByNotificationEmail(String notificationEmail);
}
