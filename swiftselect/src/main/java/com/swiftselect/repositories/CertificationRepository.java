package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.profile.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Certification findByIdAndJobSeeker_Id(Long id, Long jobSeekerId);
    List<Certification> findAllByJobSeeker(JobSeeker jobSeeker);
}