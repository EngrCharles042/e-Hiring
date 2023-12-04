package com.swiftselect.infrastructure.controllers.jobseekercontrollers;

import com.swiftselect.payload.request.jsrequests.jsprofilerequests.*;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seeker/create-profile")
public class CreateProfileController {
    private final JobSeekerService jobSeekerService;


    @PostMapping("/work-experience")
    public ResponseEntity<APIResponse<String>> newWorkExperience(@RequestBody JSWorkExperienceRequest workExperience) {
        return jobSeekerService.newWorkExperience(workExperience);
    }

    @PostMapping("/education")
    public ResponseEntity<APIResponse<String>> newEducation(@RequestBody EducationRequest educationRequest) {
        return jobSeekerService.newEducation(educationRequest);
    }

    @PostMapping("/skill")
    public ResponseEntity<APIResponse<String>> newSkills(@RequestBody SkillsRequest skillsRequest) {
        return jobSeekerService.newSkills(skillsRequest);
    }

    @PostMapping("/license")
    public ResponseEntity<APIResponse<String>> newLicense(@RequestBody LicenseRequest licenseRequest) {
        return jobSeekerService.newLicense(licenseRequest);
    }

    @PostMapping("/certification")
    public ResponseEntity<APIResponse<String>> newCertification(@RequestBody CertificationRequest certificationRequest) {
        return jobSeekerService.newCertification(certificationRequest);
    }

    @PostMapping("/language")
    public ResponseEntity<APIResponse<String>> newLanguage(@RequestBody LanguageRequest languageRequest) {
        return jobSeekerService.newLanguage(languageRequest);
    }

    @PostMapping("/job-preference")
    public ResponseEntity<APIResponse<String>> newJobPreference(@RequestBody JobPreferenceRequest preferenceRequest) {
        return jobSeekerService.newJobPreference(preferenceRequest);
    }
}
