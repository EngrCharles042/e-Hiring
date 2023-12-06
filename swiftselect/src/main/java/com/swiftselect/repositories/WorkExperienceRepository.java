package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.profile.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    WorkExperience findByIdAndJobSeeker_Id(Long id, Long jobSeekerId);
    List<WorkExperience> findAllByJobSeeker(JobSeeker jobSeeker);
}