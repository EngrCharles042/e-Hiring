package com.swiftselect.infrastructure.controllers.employercontrollers;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.payload.request.employerreqests.EmployerProfileContactInfoRequest;
import com.swiftselect.payload.request.employerreqests.EmployerUpdateProfileRequest;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.services.EmployerService;
import com.swiftselect.services.JobPostService;
import com.swiftselect.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {
    private final EmployerService employerService;
    private final JobPostService jobPostService;
    private final ModelMapper mapper;


    @GetMapping
    public ResponseEntity<APIResponse<EmployerResponse>> getEmployer() {
        Employer employer = employerService.getEmployer();
        System.out.println(employer.getFirstName());
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Retrieved Successfully",

                        mapper.map(employer, EmployerResponse.class)
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<String>> resetPassword(final HttpServletRequest request, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return employerService.resetPassword(request, resetPasswordRequest);
    }

    @PutMapping("/update-profile-company-info")
    public ResponseEntity<APIResponse<String>> updateProfileCompanyInfo(@RequestBody EmployerUpdateProfileRequest profileRequest) {
        return employerService.updateProfileCompanyInfo(profileRequest);
    }

    @PutMapping("/update-profile-contact-info")
    public ResponseEntity<APIResponse<String>> updateProfileContactInfo(@RequestBody EmployerProfileContactInfoRequest profileRequest) {
        return employerService.updateProfileContactInfo(profileRequest);
    }

    @DeleteMapping("/delete-job-post/{post-id}")
    public ResponseEntity<APIResponse<String>> deleteJobPost(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable("post-id") Long postId) {
        return employerService.deleteJobPost(userDetails.getUsername(), postId);
    }

    @GetMapping("/all")
    public ResponseEntity<EmployerResponsePage> getAllEmployers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) String sortDir){
        return employerService.getAllEmployers(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/job-posts")
    public ResponseEntity<APIResponse<List<JobPostResponse>>> getAllJobPostsByEmployer() {
        return jobPostService.findJobPostsByEmployer();
    }
}
