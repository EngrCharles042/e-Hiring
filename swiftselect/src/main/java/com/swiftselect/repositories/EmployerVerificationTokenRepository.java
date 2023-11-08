package com.swiftselect.repositories;

import com.swiftselect.domain.entities.EmployerVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerVerificationTokenRepository extends JpaRepository<EmployerVerificationToken, Long> {
    Optional<EmployerVerificationToken> findByToken(String token);
}
