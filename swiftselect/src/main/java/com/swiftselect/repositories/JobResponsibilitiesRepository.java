package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobResponsibilitiesRepository extends JpaRepository<JobResponsibilities,Long> {
    JobResponsibilities findByJobPost_Id(Long id);
}
