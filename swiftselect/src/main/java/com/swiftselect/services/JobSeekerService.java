package com.swiftselect.services;

import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.request.jsrequests.jsprofilerequests.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface JobSeekerService {
     ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);

     // CREATING NEW PROFILE

     ResponseEntity<String> newWorkExperience(JSWorkExperienceRequest workExperience);

     ResponseEntity<String> newEducation(EducationRequest educationRequest);

     ResponseEntity<String> newSkills(SkillsRequest skillsRequest);

     ResponseEntity<String> newLicense(LicenseRequest licenseRequest);

     ResponseEntity<String> newCertification(CertificationRequest certificationRequest);

     ResponseEntity<String> newLanguage(LanguageRequest languageRequest);

     ResponseEntity<String> newJobPreference(JobPreferenceRequest preferenceRequest);

}
