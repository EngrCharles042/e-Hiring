package com.swiftselect.infrastructure.controllers.JobPostControllers;

import com.swiftselect.payload.request.jobPostRequests.JobPostRequest;
import com.swiftselect.payload.request.jobPostRequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobPostRequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobPostRequests.QualificationRequest;
import com.swiftselect.services.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-post")
public class JobPostController {
    private final JobPostService jobPostService;


    // CREATE JOB POST

    @PostMapping("/create-job-post")
    public ResponseEntity<String> createJobPost (@Valid @RequestBody JobPostRequest jobPostRequest){

        return jobPostService.createJobPost(jobPostRequest);
    }

    @PostMapping("/{jobPostId}/responsibilities")
    public ResponseEntity<String> addResponsibilitiesToJobPost(@PathVariable Long jobPostId,
                                                               @Valid @RequestBody Set<JobResponsibilitiesRequest> responsibilitiesRequest){

        return jobPostService.addResponsibilitiesToJobPost(jobPostId, responsibilitiesRequest);
    }

    @PostMapping("/{jobPostId}/qualifications")
    public ResponseEntity<String> addQualificationsToJobPost(@PathVariable Long jobPostId,
                                                             @Valid @RequestBody Set<QualificationRequest> qualificationRequest){

        return jobPostService.addQualificationToJobPost(jobPostId, qualificationRequest);
    }

    @PostMapping("/{jobPostId}/nice_to_haves")
    public ResponseEntity<String> addNiceToHavesToJobPost(@PathVariable Long jobPostId,
                                                          @Valid @RequestBody Set<NiceToHaveRequest> niceToHaveRequest) {

        return jobPostService.addNiceToHaveToJobPost(jobPostId, niceToHaveRequest);
    }


    // UPDATE JOB POST

    @PutMapping("/{jobPostId}/update-job-post")
    public ResponseEntity<String> updateJobPost (@PathVariable Long jobPostId,
                                                 @Valid @RequestBody JobPostRequest jobPostRequest) {

        return jobPostService.updateJobPost(jobPostId, jobPostRequest);
    }

    @PutMapping("/{jobPostId}/responsibilities")
    public ResponseEntity<String> updateResponsibilitiesToJobPost(@PathVariable Long jobPostId,
                                                               @Valid @RequestBody Set<JobResponsibilitiesRequest> responsibilitiesRequest){

        return jobPostService.updateResponsibilitiesToJobPost(jobPostId, responsibilitiesRequest);
    }

    @PutMapping("/{jobPostId}/qualifications")
    public ResponseEntity<String> updateQualificationsToJobPost(@PathVariable Long jobPostId,
                                                             @Valid @RequestBody Set<QualificationRequest> qualificationRequest){

        return jobPostService.updateQualificationToJobPost(jobPostId, qualificationRequest);
    }

    @PutMapping("/{jobPostId}/nice_to_haves")
    public ResponseEntity<String> updateNiceToHavesToJobPost(@PathVariable Long jobPostId,
                                                          @Valid @RequestBody Set<NiceToHaveRequest> niceToHaveRequest) {

        return jobPostService.updateNiceToHaveToJobPost(jobPostId, niceToHaveRequest);
    }
}
