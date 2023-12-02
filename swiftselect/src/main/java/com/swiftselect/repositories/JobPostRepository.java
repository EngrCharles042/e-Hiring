package com.swiftselect.repositories;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.enums.EmploymentType;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.JobType;
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
    Slice<JobPost> findAllByExperienceLevel(ExperienceLevel experienceLevel, Pageable pageable);
    List<JobPost> findAllByExperienceLevel(ExperienceLevel experienceLevel);
    List<JobPost> findJobPostsByJobTypeOrJobTypeOrJobTypeOrEmploymentTypeOrEmploymentTypeOrEmploymentTypeOrEmploymentTypeOrExperienceLevelOrExperienceLevelOrExperienceLevelOrExperienceLevelOrExperienceLevel(JobType jobType,
                                                                                                                                                                                                               JobType jobType2,
                                                                                                                                                                                                               JobType jobType3,
                                                                                                                                                                                                               EmploymentType employmentType,
                                                                                                                                                                                                               EmploymentType employmentType2,
                                                                                                                                                                                                               EmploymentType employmentType3,
                                                                                                                                                                                                               EmploymentType employmentType4,
                                                                                                                                                                                                               ExperienceLevel experienceLevel,
                                                                                                                                                                                                               ExperienceLevel experienceLevel2,
                                                                                                                                                                                                               ExperienceLevel experienceLevel3,
                                                                                                                                                                                                               ExperienceLevel experienceLevel4,
                                                                                                                                                                                                               ExperienceLevel experienceLevel5);
}
