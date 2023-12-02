package com.swiftselect.infrastructure.controllers.jobseekercontrollers;

import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.authresponse.ResetPasswordResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerInfoResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponsePage;
import com.swiftselect.services.JobSeekerService;
import com.swiftselect.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seeker")
public class JobSeekerController {
    private final JobSeekerService jobSeekerService;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<APIResponse<JobSeekerInfoResponse>> getJobSeeker() {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker();
        System.out.println(jobSeeker.getFirstName());
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Retrieved Successfully",
                        mapper.map(jobSeeker, JobSeekerInfoResponse.class)
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
}
