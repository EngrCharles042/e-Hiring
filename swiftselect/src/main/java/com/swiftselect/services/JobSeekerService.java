package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;

public interface JobSeekerService {
    void saveVerificationToken(JobSeeker jobSeeker, String token);
    String validateToken(String token);
}
