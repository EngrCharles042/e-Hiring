package com.swiftselect.infrastructure.controllers.jobpostcontrollers;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.ReportCat;
import com.swiftselect.payload.request.jobpostrequests.JobPostRequest;
import com.swiftselect.payload.request.jobpostrequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobpostrequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobpostrequests.QualificationRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.PostResponsePage;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.payload.response.jobpostresponse.JobSearchResponse;
import com.swiftselect.repositories.JobPostRepository;
import com.swiftselect.services.JobPostService;
import com.swiftselect.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-post")
public class JobPostController {
    private final JobPostService jobPostService;

    // GET ALL JOB POSTS
    @GetMapping
    public ResponseEntity<APIResponse<PostResponsePage>> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) String sortDir) {

        return jobPostService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // CREATE JOB POST

    @PostMapping("/create-job-post")
    public ResponseEntity<APIResponse<JobPostResponse>> createJobPost (@Valid @RequestBody JobPostRequest jobPostRequest){

        return jobPostService.createJobPost(jobPostRequest);
    }

    @PostMapping("/{jobPostId}/responsibilities")
    public ResponseEntity<APIResponse<String>> addResponsibilitiesToJobPost(@PathVariable Long jobPostId,
                                                                            @Valid @RequestBody Set<JobResponsibilitiesRequest> responsibilitiesRequest){

        return jobPostService.addResponsibilitiesToJobPost(jobPostId, responsibilitiesRequest);
    }

    @PostMapping("/{jobPostId}/qualifications")
    public ResponseEntity<APIResponse<String>> addQualificationsToJobPost(@PathVariable Long jobPostId,
                                                             @Valid @RequestBody Set<QualificationRequest> qualificationRequest){

        return jobPostService.addQualificationToJobPost(jobPostId, qualificationRequest);
    }

    @PostMapping("/{jobPostId}/nice_to_haves")
    public ResponseEntity<APIResponse<String>> addNiceToHavesToJobPost(@PathVariable Long jobPostId,
                                                          @Valid @RequestBody Set<NiceToHaveRequest> niceToHaveRequest) {

        return jobPostService.addNiceToHaveToJobPost(jobPostId, niceToHaveRequest);
    }


    // UPDATE JOB POST

    @PutMapping("/{jobPostId}/update-job-post")
    public ResponseEntity<APIResponse<String>> updateJobPost (@PathVariable Long jobPostId,
                                                 @Valid @RequestBody JobPostRequest jobPostRequest) {

        return jobPostService.updateJobPost(jobPostId, jobPostRequest);
    }

    @PutMapping("/{jobPostId}/responsibilities")
    public ResponseEntity<APIResponse<String>> updateResponsibilitiesToJobPost(@PathVariable Long jobPostId,
                                                               @Valid @RequestBody Set<JobResponsibilitiesRequest> responsibilitiesRequest){

        return jobPostService.updateResponsibilitiesToJobPost(jobPostId, responsibilitiesRequest);
    }

    @PutMapping("/{jobPostId}/qualifications")
    public ResponseEntity<APIResponse<String>> updateQualificationsToJobPost(@PathVariable Long jobPostId,
                                                             @Valid @RequestBody Set<QualificationRequest> qualificationRequest){

        return jobPostService.updateQualificationToJobPost(jobPostId, qualificationRequest);
    }

    @PutMapping("/{jobPostId}/nice_to_haves")
    public ResponseEntity<APIResponse<String>> updateNiceToHavesToJobPost(@PathVariable Long jobPostId,
                                                          @Valid @RequestBody Set<NiceToHaveRequest> niceToHaveRequest) {

        return jobPostService.updateNiceToHaveToJobPost(jobPostId, niceToHaveRequest);
    }

    @PostMapping("/{jobId}/report")
    public ResponseEntity<APIResponse<String>> reportJobPost(@PathVariable Long jobId,
                                                             @RequestParam String comment,
                                                             @RequestParam ReportCat reportCategory) {
        return jobPostService.reportJobPost(jobId, comment, reportCategory);
    }

    @GetMapping("/jobType")
    public ResponseEntity<APIResponse<List<JobPost>>> getJobPostByJobType(@RequestParam JobType jobType) {

        return jobPostService.getJobPostByJobType(jobType);
    }

    @GetMapping("/by-experience-level/{experienceLevel}")
    public ResponseEntity<APIResponse<Slice<JobPostResponse>>> getJobPostsByExperienceLevel(
            @PathVariable ExperienceLevel experienceLevel,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) String sortDir) {

        return jobPostService.getJobPostByExperienceLevel(
                experienceLevel, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<JobSearchResponse>>> jobSearchWithKeyword (@RequestParam("query")String query) {
        return jobPostService.searchJobs(query);
    }

    @GetMapping("/search_job_post")
    public ResponseEntity<APIResponse<List<JobPost>>> searchJobPost(@RequestParam("query") String query, JobType jobType, Industry jobCategory){

        return jobPostService.searchJobPost(query, jobType, jobCategory);
    }
}
