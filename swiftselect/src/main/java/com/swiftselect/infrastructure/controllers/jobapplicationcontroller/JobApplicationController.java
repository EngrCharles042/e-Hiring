package com.swiftselect.infrastructure.controllers.jobapplicationcontroller;

import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.jobpostresponse.JobApplicationResponse;
import com.swiftselect.services.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seeker")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping("/submit-application")
    public ResponseEntity<APIResponse<JobApplicationResponse>> submitJobApplication(@RequestParam long jobPostId){
        return ResponseEntity
                .status(HttpStatus.SC_CREATED)
                .body(jobApplicationService.jobApplication(jobPostId));
    }
}
