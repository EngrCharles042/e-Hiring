package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface JobResponsibilitiesRepository extends JpaRepository<JobResponsibilities,Long> {
    List<JobResponsibilities> findByJobPost_Id(Long id);

    List<JobResponsibilities> findAllByJobPost(JobPost jobPost);
}
