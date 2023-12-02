package com.swiftselect.services;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.ReportCat;
import com.swiftselect.payload.request.jobpostrequests.*;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.PostResponsePage;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.payload.response.jobpostresponse.JobSearchResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface JobPostService {
    ResponseEntity<APIResponse<JobPostResponse>> createJobPost(JobPostRequest jobPostRequest);

    ResponseEntity<APIResponse<String>> addResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest);

    ResponseEntity<APIResponse<String>> addQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationReques);

    ResponseEntity<APIResponse<String>> addNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest);

    ResponseEntity<APIResponse<String>> updateJobPost(Long postId, JobPostRequest jobPostRequest);

    ResponseEntity<APIResponse<String>> updateResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest);

    ResponseEntity<APIResponse<String>> updateQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest);

    ResponseEntity<APIResponse<String>> updateNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest);

    ResponseEntity<APIResponse<Slice<JobPostResponse>>> getJobPostByExperienceLevel(ExperienceLevel experienceLevel, int pageNo, int pageSize, String sortBy, String sortDir);

    ResponseEntity<APIResponse<PostResponsePage>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    ResponseEntity<APIResponse<String>> reportJobPost(Long jobId, String comment, ReportCat reportCategory);

    ResponseEntity<APIResponse<List<JobPost>>> getJobPostByJobType(JobType jobType);

    ResponseEntity<APIResponse<JobPostResponse>> getJobPostById(Long id);
    ResponseEntity<APIResponse<List<JobSearchResponse>>> searchJobs(String query);

    ResponseEntity<APIResponse<List<JobPost>>>  searchJobPost(String query, JobType jobType, Industry jobCategory);

    ResponseEntity<APIResponse<List<JobPost>>> getJobPostByStateAndCountry(String state, String country);
}
