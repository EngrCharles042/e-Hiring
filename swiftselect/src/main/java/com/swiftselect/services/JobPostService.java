package com.swiftselect.services;

import com.swiftselect.payload.request.jobPostRequests.JobPostRequest;
import com.swiftselect.payload.request.jobPostRequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobPostRequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobPostRequests.QualificationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface JobPostService {
    ResponseEntity<String> createJobPost(JobPostRequest jobPostRequest);

    ResponseEntity<String> addResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest);

    ResponseEntity<String> addQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest);

    ResponseEntity<String> addNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest);
}
