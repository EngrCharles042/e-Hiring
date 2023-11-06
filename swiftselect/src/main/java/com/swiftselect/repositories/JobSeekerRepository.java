package com.swiftselect.repositories;

import com.swiftselect.domain.entities.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    boolean existsByEmail(String email);

    Optional<JobSeeker> findByEmail(String email);

=======
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    boolean existsByEmail(String email);

>>>>>>> develop
}
