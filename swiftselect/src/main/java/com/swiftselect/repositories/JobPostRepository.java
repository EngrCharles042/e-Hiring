package com.swiftselect.repositories;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.enums.EmploymentType;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.PayRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Optional<JobPost> findByIdAndEmployer(Long id, Employer employer);
    Page<JobPost> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<JobPost> findAllByJobType(JobType jobType);
}
