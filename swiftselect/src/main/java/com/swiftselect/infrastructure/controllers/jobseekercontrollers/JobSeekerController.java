package com.swiftselect.infrastructure.controllers.jobseekercontrollers;

import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.enums.EmploymentType;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.request.jobpostrequests.ReportJobPostRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.authresponse.ResetPasswordResponse;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerInfoResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponsePage;
import com.swiftselect.repositories.JobPostRepository;
import com.swiftselect.services.JobPostService;
import com.swiftselect.services.JobSeekerService;
import com.swiftselect.utils.AppConstants;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seeker")
public class JobSeekerController {
    private final JobSeekerService jobSeekerService;
    private final JobPostService jobPostService;
    private final HelperClass helperClass;
    private final JobPostRepository jobPostRepository;

    @GetMapping
    public ResponseEntity<APIResponse<JobSeekerInfoResponse>> getJobSeeker() {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker();
        System.out.println(jobSeeker.getFirstName());
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Retrieved Successfully",
                        helperClass.jobSeekerToJobSeekerInfoResponse(jobSeeker)
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<ResetPasswordResponse>> resetPassword(final HttpServletRequest request, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return jobSeekerService.resetPassword(request, resetPasswordRequest);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<APIResponse<String>> deleteMyAccount(){
        jobSeekerService.deleteMyAccount();

        return ResponseEntity.ok(new APIResponse<>("Account deleted successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<JobSeekerResponsePage>> getAllEmployers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
                                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) String sortDir){

        return jobSeekerService.getAllJobSeekers(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("/report")
    public ResponseEntity<APIResponse<String>> reportJobPost(@Valid @RequestBody ReportJobPostRequest reportJobPostRequest) {
        return jobPostService.reportJobPost(reportJobPostRequest.getJobId(), reportJobPostRequest.getComment(), reportJobPostRequest.getReportCategory());
    }

    @PostMapping("/employer")
    public ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPostByCompanyId(@RequestParam Long employerId) {
        return jobPostService.getJobPostByEmployerId(employerId);
    }

    @PostMapping("/filter")
    public ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPosts(
            @RequestParam(required = false) JobType REMOTE,
            @RequestParam(required = false) JobType HYBRID,
            @RequestParam(required = false) JobType ON_SITE,
            @RequestParam(required = false) EmploymentType FULL_TIME,
            @RequestParam(required = false) EmploymentType PART_TIME,
            @RequestParam(required = false) EmploymentType CONTRACT,
            @RequestParam(required = false) EmploymentType TEMPORARY,
            @RequestParam(required = false) ExperienceLevel ENTRY_LEVEL,
            @RequestParam(required = false) ExperienceLevel JUNIOR_LEVEL,
            @RequestParam(required = false) ExperienceLevel MID_LEVEL,
            @RequestParam(required = false) ExperienceLevel SENIOR_LEVEL,
            @RequestParam(required = false) ExperienceLevel EXPERT_LEVEL
    ) {

        return ResponseEntity.ok(
                new APIResponse<>(
                        "successful",
                        jobPostRepository.findJobPostsByJobTypeOrJobTypeOrJobTypeOrEmploymentTypeOrEmploymentTypeOrEmploymentTypeOrEmploymentTypeOrExperienceLevelOrExperienceLevelOrExperienceLevelOrExperienceLevelOrExperienceLevelOrderByCreateDateDesc(
                                        REMOTE,
                                        HYBRID,
                                        ON_SITE,
                                        FULL_TIME,
                                        PART_TIME,
                                        CONTRACT,
                                        TEMPORARY,
                                        ENTRY_LEVEL,
                                        JUNIOR_LEVEL,
                                        MID_LEVEL,
                                        SENIOR_LEVEL,
                                        EXPERT_LEVEL
                                ).stream()
                                .map(jobPostService::jobPostToJobPostResponse
                                )
                                .toList()
                )
        );
    }
}
