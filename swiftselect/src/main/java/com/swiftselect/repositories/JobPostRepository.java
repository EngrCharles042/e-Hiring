package com.swiftselect.repositories;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Optional<JobPost> findByIdAndEmployer(Long id, Employer employer);
}