package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<Applications, Long> {
    Optional<Applications> findByJobSeekerEmail(String email);

}
