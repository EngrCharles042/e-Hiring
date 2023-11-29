package com.swiftselect.repositories;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.domain.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Optional<JobPost> findByIdAndEmployer(Long id, Employer employer);
    Page<JobPost> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<JobPost> findAllByJobType(JobType jobType);

    Slice<JobPost> findAllByExperienceLevel(ExperienceLevel experienceLevel, Pageable pageable);
    List<JobPost> findAllByExperienceLevel(ExperienceLevel experienceLevel);

    @Query("SELECT p FROM JobPost p " +
            "WHERE p.title LIKE CONCAT('%', :query, '%') OR " +
            "p.jobType = :jobType OR " +
            "p.jobCategory = :jobCategory")
    List<JobPost> searchJobs(String query, JobType jobType, Industry jobCategory);
}
