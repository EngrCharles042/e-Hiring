package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NiceToHaveRepository extends JpaRepository<NiceToHave, Long> {
    List<NiceToHave> findAllByJobPost(JobPost jobPost);
}
