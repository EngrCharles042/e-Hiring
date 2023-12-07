package com.swiftselect.services;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.subcriber.Subscriber;
import com.swiftselect.domain.entities.notification.Notification;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.infrastructure.event.events.JobPostCreatedEvent;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.request.jsrequests.jsprofilerequests.*;
import com.swiftselect.payload.request.notificationRequest.SubscriptionRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.NotificationResponse;
import com.swiftselect.payload.response.authresponse.ResetPasswordResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponsePage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobSeekerService {
     ResponseEntity<APIResponse<ResetPasswordResponse>> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);
     void deleteMyAccount();

     JobSeeker getJobSeeker();

     // UPDATE PROFILE

     ResponseEntity<APIResponse<String>> contactInfoUpdate(JSContactInfoRequest contactInfoRequest);

     ResponseEntity<APIResponse<String>> locationInfoUpdate(JSLocationInfoRequest locationInfoRequest);

     ResponseEntity<APIResponse<String>> resumeUpdate(MultipartFile resume);
     ResponseEntity<APIResponse<String>> coverLetterUpdate(MultipartFile coverLetter);

     ResponseEntity<APIResponse<String>> workExperienceUpdate(JSWorkExperienceRequest workExperience, long id);

     ResponseEntity<APIResponse<String>> educationUpdate(EducationRequest educationRequest, long id);

     ResponseEntity<APIResponse<String>> skillsUpdate(SkillsRequest skillsRequest, long id);

     ResponseEntity<APIResponse<String>> licenseUpdate(LicenseRequest licenseRequest, long id);

     ResponseEntity<APIResponse<String>> certificationUpdate(CertificationRequest certificationRequest, long id);

     ResponseEntity<APIResponse<String>> languageUpdate(LanguageRequest languageRequest, long id);

     ResponseEntity<APIResponse<String>> jobPreferenceUpdate(JobPreferenceRequest preferenceRequest, long id);

     ResponseEntity<APIResponse<String>> jobExpectationUpdate(JobExpectationsRequest jobExpectationsRequest);

     ResponseEntity<APIResponse<String>> socialsUpdate(JSSocialsRequests socialsRequests);


     // CREATING NEW PROFILE

     ResponseEntity<APIResponse<String>> newWorkExperience(JSWorkExperienceRequest workExperience);

     ResponseEntity<APIResponse<String>> newEducation(EducationRequest educationRequest);

     ResponseEntity<APIResponse<String>> newSkills(SkillsRequest skillsRequest);

     ResponseEntity<APIResponse<String>> newLicense(LicenseRequest licenseRequest);

     ResponseEntity<APIResponse<String>> newCertification(CertificationRequest certificationRequest);

     ResponseEntity<APIResponse<String>> newLanguage(LanguageRequest languageRequest);

     ResponseEntity<APIResponse<String>> newJobPreference(JobPreferenceRequest preferenceRequest);

     ResponseEntity<APIResponse<JobSeekerResponsePage>> getAllJobSeekers(int pageNo, int pageSize, String sortBy, String sortDir);




     List<JobSeeker> getSubscribersByIndustry(Industry industry);

     void subscribeJobSeekerToIndustry(List<Industry> industries);

     boolean isSubscribed(String jobSeekerId, Industry industry);

     void subscribe(Subscriber subscriber);

     void handleJobPostCreatedEvent(JobPostCreatedEvent event);

     void sendNotification(Notification notification);

     void markNotificationAsRead(Long notificationId);

     ResponseEntity<APIResponse<List<NotificationResponse>>>  getNotifications();
}
