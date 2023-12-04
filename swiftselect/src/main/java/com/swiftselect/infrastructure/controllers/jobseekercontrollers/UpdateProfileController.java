package com.swiftselect.infrastructure.controllers.jobseekercontrollers;

import com.swiftselect.payload.request.CoverLetterRequest;
import com.swiftselect.payload.request.ResumeRequest;
import com.swiftselect.payload.request.jsrequests.jsprofilerequests.*;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.services.JobSeekerService;
import com.swiftselect.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seeker/update-profile")
public class UpdateProfileController {
    private final JobSeekerService jobSeekerService;

    // PERSONAL INFORMATION

    @PutMapping("/contact-info")
    public ResponseEntity<APIResponse<String>> contactInfoUpdate(@RequestBody JSContactInfoRequest contactInfoRequest) {

        return jobSeekerService.contactInfoUpdate(contactInfoRequest);
    }

    @PutMapping("/location-info")
    public ResponseEntity<APIResponse<String>> locationInfoUpdate(@RequestBody JSLocationInfoRequest locationInfoRequest) {

        return jobSeekerService.locationInfoUpdate(locationInfoRequest);
    }

    @PutMapping("/resume")
    public ResponseEntity<APIResponse<String>> resumeUpdate(@RequestParam MultipartFile resume) {

        if (resume.getSize() > AppConstants.MAX_FILE_SIZE) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("FIle size exceed the normal limit"));
        }

        return jobSeekerService.resumeUpdate(resume);
    }

    @PutMapping("/cover-letter")
    public ResponseEntity<APIResponse<String>> coverLetterUpdate(@RequestParam MultipartFile coverLetter) {

        if (coverLetter.getSize() > AppConstants.MAX_FILE_SIZE) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("FIle size exceed the normal limit"));
        }

        return jobSeekerService.coverLetterUpdate(coverLetter);
    }

    @PutMapping("/socials")
    public ResponseEntity<APIResponse<String>> socialsUpdate(@RequestBody JSSocialsRequests socialsRequests) {

        return jobSeekerService.socialsUpdate(socialsRequests);
    }

    // QUALIFICATIONS

    @PutMapping("/work-experience/{id}")
    public ResponseEntity<APIResponse<String>> workExperienceUpdate(@RequestBody JSWorkExperienceRequest workExperience,
                                                       @PathVariable("id") long id) {

        return jobSeekerService.workExperienceUpdate(workExperience, id);
    }

    @PutMapping("/education/{id}")
    public ResponseEntity<APIResponse<String>> educationUpdate(@RequestBody EducationRequest educationRequest,
                                                  @PathVariable("id") long id) {

        return jobSeekerService.educationUpdate(educationRequest, id);
    }

    @PutMapping("/skill/{id}")
    public ResponseEntity<APIResponse<String>> skillsUpdate(@RequestBody SkillsRequest skillsRequest,
                                               @PathVariable("id") long id) {

        return jobSeekerService.skillsUpdate(skillsRequest, id);
    }

    @PutMapping("/license/{id}")
    public ResponseEntity<APIResponse<String>> licenseUpdate(@RequestBody LicenseRequest licenseRequest,
                                                @PathVariable("id") long id) {

        return jobSeekerService.licenseUpdate(licenseRequest, id);
    }

    @PutMapping("/certification/{id}")
    public ResponseEntity<APIResponse<String>> certificationUpdate(@RequestBody CertificationRequest certificationRequest,
                                                      @PathVariable("id") long id) {

        return jobSeekerService.certificationUpdate(certificationRequest, id);
    }

    @PutMapping("/language/{id}")
    public ResponseEntity<APIResponse<String>> languageUpdate(@RequestBody LanguageRequest languageRequest,
                                                 @PathVariable("id") long id) {

        return jobSeekerService.languageUpdate(languageRequest, id);
    }

    // JOB PREFERENCES

    @PutMapping("/job-preference/{id}")
    public ResponseEntity<APIResponse<String>> jobPreferenceUpdate(@RequestBody JobPreferenceRequest preferenceRequest,
                                                      @PathVariable("id") long id) {

        return jobSeekerService.jobPreferenceUpdate(preferenceRequest, id);
    }

    @PutMapping("/job-expectation")
    public ResponseEntity<APIResponse<String>> jobExpectationUpdate(@RequestBody JobExpectationsRequest jobExpectationsRequest) {

        return jobSeekerService.jobExpectationUpdate(jobExpectationsRequest);
    }
}
