package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.payload.request.EmployerSignup;
import com.swiftselect.payload.request.JobSeekerSignup;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JobSeeker> registerJobSeeker(JobSeekerSignup jobSeekerSignup);
    ResponseEntity<Employer> registerEmployer(EmployerSignup employerSignup);


}
