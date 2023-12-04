package com.swiftselect.repositories;

import com.swiftselect.domain.entities.jobseeker.subcriber.Subscriber;
import com.swiftselect.domain.enums.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {
    List<Subscriber> findBySubscribedIndustry(Industry subscribedIndustry);

    boolean existsByJobSeeker_EmailAndSubscribedIndustry(String email, Industry industry);
}
