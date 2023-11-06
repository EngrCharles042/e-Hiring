package com.swiftselect.repositories;

import com.swiftselect.domain.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByWorkEmail(String workEmail);
=======
public interface EmployerRepository extends JpaRepository<Employer, Long> {
>>>>>>> develop
    boolean existsByWorkEmail(String workEmail);
    boolean existsByNotificationEmail(String notificationEmail);
}
