package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<Qualification,Long> {
}
