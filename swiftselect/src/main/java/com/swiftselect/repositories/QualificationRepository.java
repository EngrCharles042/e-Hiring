package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobpost.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    List<Qualification> findAllByJobPost(JobPost jobPost);
}
