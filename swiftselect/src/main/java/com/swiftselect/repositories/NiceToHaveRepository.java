package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.NiceToHave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiceToHaveRepository extends JpaRepository<NiceToHave,Long> {
}
