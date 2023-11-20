package com.swiftselect.services;

import com.swiftselect.payload.request.jobPostRequests.JobPostRequest;
import com.swiftselect.payload.request.jobPostRequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobPostRequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobPostRequests.QualificationRequest;
import com.swiftselect.payload.response.JobPostResponse;
import com.swiftselect.payload.response.PostResponsePage;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface JobPostService {
    ResponseEntity<String> createJobPost(JobPostRequest jobPostRequest);

    ResponseEntity<String> addResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest);

    ResponseEntity<String> addQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest);

    ResponseEntity<String> addNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest);

    ResponseEntity<String> updateJobPost(Long postId, JobPostRequest jobPostRequest);

    ResponseEntity<String> updateResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest);

    ResponseEntity<String> updateQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest);

    ResponseEntity<String> updateNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest);

    ResponseEntity<PostResponsePage> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
