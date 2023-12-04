package com.swiftselect.repositories;

import com.swiftselect.domain.entities.Report;
import com.swiftselect.domain.entities.employer.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByJobPostEmployer(Employer jobPost_employer);

    Boolean existsByJobSeekerId (Long jobSeeker_id);

}
