package com.swiftselect.services;

import com.swiftselect.domain.entities.employer.Employer;
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

    ResponseEntity<APIResponse<Slice<JobPostResponse>>> getJobPostByExperienceLevel(ExperienceLevel experienceLevel, int pageNo, int pageSize, String sortBy, String sortDir);

    ResponseEntity<APIResponse<PostResponsePage>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    ResponseEntity<APIResponse<String>> reportJobPost(Long jobId, String comment, ReportCat reportCategory);

    ResponseEntity<APIResponse<List<JobPost>>> getJobPostByJobType(JobType jobType);

    ResponseEntity<APIResponse<JobPostResponse>> getJobPostById(Long id);

    ResponseEntity<APIResponse<List<JobPostResponse>>> searchJobs(String query);

    JobPostResponse jobPostToJobPostResponse(JobPost jobPost);

    ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPostByStateAndCountry(String query);

    ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPostByEmployerId(Long id);

    ResponseEntity<APIResponse<List<JobPostResponse>>> findJobPostsByEmployer();
}
