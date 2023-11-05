package com.swiftselect.repositories;

import com.swiftselect.domain.entities.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    boolean existsByEmail(String email);

}
