package com.swiftselect.services.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.profile.*;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
import com.swiftselect.payload.request.jsrequests.jsprofilerequests.*;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.authresponse.ResetPasswordResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerListResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponsePage;
import com.swiftselect.repositories.*;
import com.swiftselect.services.FileUpload;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final EducationRepository educationRepository;
    private final JobPreferenceRepository jobPreferenceRepository;
    private final LanguageRepository languageRepository;
    private final LicenseRepository licenseRepository;
    private final SkillsRepository skillsRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final AuthenticationManager authenticationManager;
    private final JobSeekerRepository jobSeekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HelperClass helperClass;
    private final EventPublisher eventPublisher;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;
    private final CertificationRepository certificationRepository;
    private final FileUploadImpl fileUpload;


    public JobSeeker getJobSeeker() {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        return  jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email));
    }


    @Override
    public ResponseEntity<APIResponse<ResetPasswordResponse>> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        String email = jobSeeker.getEmail();

        // Authentication manager to authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        resetPasswordRequest.getOldPassword()
                )
        );

        jobSeeker.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        jobSeekerRepository.save(jobSeeker);

        eventPublisher.notificationMailEventPublisher(email,
                jobSeeker.getFirstName(), "Password Reset",
                "Password successfully changed. If you did not initiate this, please send a reply to this email",
                request);

        ResetPasswordResponse resetResponse = ResetPasswordResponse.builder()
                .id(jobSeeker.getId())
                .firstName(jobSeeker.getFirstName())
                .build();

        return ResponseEntity.ok(new APIResponse<>("Password successfully changed", resetResponse));
    }

    @Override
    public void deleteMyAccount() {
        JobSeeker jobSeeker = getJobSeeker();

        if (jobSeeker!=null) {
            jobSeekerRepository.delete(jobSeeker);
        }
    }

    @Override
    public ResponseEntity<APIResponse<String>> contactInfoUpdate(JSContactInfoRequest contactInfoRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        jobSeeker.setFirstName(contactInfoRequest.getFirstName());
        jobSeeker.setLastName(contactInfoRequest.getLastName());
        jobSeeker.setPhoneNumber(contactInfoRequest.getPhoneNumber());

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> locationInfoUpdate(JSLocationInfoRequest locationInfoRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        jobSeeker.setState(locationInfoRequest.getState());
        jobSeeker.setCity(locationInfoRequest.getCity());
        jobSeeker.setAddress(locationInfoRequest.getAddress());
        jobSeeker.setPostalCode(locationInfoRequest.getPostalCode());

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> resumeUpdate(MultipartFile resume) {

        JobSeeker jobSeeker = getJobSeeker();

        String fileUrl;

        try {
            fileUrl = fileUpload.uploadFile(resume);

            jobSeeker.setResume(fileUrl);

        } catch (IOException e) {
            throw new ApplicationException("File upload error, please try again");
        }

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> coverLetterUpdate(MultipartFile coverLetter) {

        JobSeeker jobSeeker = getJobSeeker();

        String fileUrl;

        try {
            fileUrl = fileUpload.uploadFile(coverLetter);

            jobSeeker.setCoverLetter(fileUrl);
        } catch (IOException e) {
            throw new ApplicationException("File upload error, please try again");
        }

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> workExperienceUpdate(JSWorkExperienceRequest workExperienceRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        WorkExperience workExperience = workExperienceRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (workExperience != null) {
            workExperience.setJobTitle(workExperienceRequest.getJobTitle());
            workExperience.setCompanyName(workExperienceRequest.getCompanyName());
            workExperience.setStartDate(workExperienceRequest.getStartDate());
            workExperience.setStopDate(workExperienceRequest.getStopDate());

            workExperienceRepository.save(workExperience);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> educationUpdate(EducationRequest educationRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        Education education = educationRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (education != null) {
            education.setEducationLevel(educationRequest.getEducationLevel());
            education.setFieldOfStudy(educationRequest.getFieldOfStudy());
            education.setYearOfGraduation(educationRequest.getYearOfGraduation());

            educationRepository.save(education);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> skillsUpdate(SkillsRequest skillsRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        Skills skills = skillsRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (skills != null) {
            skills.setSkill(skillsRequest.getSkill());
            skills.setYearsOfExperience(skillsRequest.getYearsOfExperience());

            skillsRepository.save(skills);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> licenseUpdate(LicenseRequest licenseRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        License license = licenseRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (license != null) {
            license.setLicenseName(licenseRequest.getLicenseName());
            license.setExpirationDate(licenseRequest.getExpirationDate());

            licenseRepository.save(license);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> certificationUpdate(CertificationRequest certificationRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        Certification certification = certificationRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (certification != null) {
            certification.setName(certificationRequest.getName());
            certification.setExpiration(certificationRequest.getExpiration());

            certificationRepository.save(certification);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> languageUpdate(LanguageRequest languageRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        Language language = languageRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (language != null) {
            language.setLanguage(languageRequest.getLanguage());
            language.setProficiency(languageRequest.getProficiency());

            languageRepository.save(language);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> jobPreferenceUpdate(JobPreferenceRequest preferenceRequest, long id) {
        JobSeeker jobSeeker = getJobSeeker();

        JobPreference jobPreference = jobPreferenceRepository.findByIdAndJobSeeker_Id(id, jobSeeker.getId());

        if (jobPreference != null) {
            jobPreference.setIndustry(preferenceRequest.getIndustry());

            jobPreferenceRepository.save(jobPreference);

            return ResponseEntity.ok(new APIResponse<>("update successful"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>("not found"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> jobExpectationUpdate(JobExpectationsRequest jobExpectationsRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        jobSeeker.setWorkSchedule(jobExpectationsRequest.getWorkSchedule());
        jobSeeker.setBasePay(jobExpectationsRequest.getBasePay());
        jobSeeker.setPayRate(jobExpectationsRequest.getPayRate());
        jobSeeker.setJobType(jobExpectationsRequest.getJobType());

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> socialsUpdate(JSSocialsRequests socialsRequests) {
        JobSeeker jobSeeker = getJobSeeker();

        jobSeeker.setFacebook(socialsRequests.getFacebook());
        jobSeeker.setTwitter(socialsRequests.getTwitter());
        jobSeeker.setInstagram(socialsRequests.getInstagram());

        jobSeekerRepository.save(jobSeeker);

        return ResponseEntity.ok(new APIResponse<>("update successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newWorkExperience(JSWorkExperienceRequest workExperienceRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        WorkExperience workExperience = modelMapper.map(workExperienceRequest, WorkExperience.class);
        workExperience.setJobSeeker(jobSeeker);

        workExperienceRepository.save(workExperience);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newEducation(EducationRequest educationRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        Education education = modelMapper.map(educationRequest, Education.class);
        education.setJobSeeker(jobSeeker);

        educationRepository.save(education);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newSkills(SkillsRequest skillsRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        Skills skills = modelMapper.map(skillsRequest, Skills.class);
        skills.setJobSeeker(jobSeeker);

        skillsRepository.save(skills);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newLicense(LicenseRequest licenseRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        License license = modelMapper.map(licenseRequest, License.class);
        license.setJobSeeker(jobSeeker);

        licenseRepository.save(license);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newCertification(CertificationRequest certificationRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        Certification certification = modelMapper.map(certificationRequest, Certification.class);
        certification.setJobSeeker(jobSeeker);

        certificationRepository.save(certification);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newLanguage(LanguageRequest languageRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        Language language = modelMapper.map(languageRequest,  Language.class);
        language.setJobSeeker(jobSeeker);

        languageRepository.save(language);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> newJobPreference(JobPreferenceRequest preferenceRequest) {
        JobSeeker jobSeeker = getJobSeeker();

        JobPreference jobPreference = modelMapper.map(preferenceRequest, JobPreference.class);
        jobPreference.setJobSeeker(jobSeeker);

        jobPreferenceRepository.save(jobPreference);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>("created successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<JobSeekerResponsePage>> getAllJobSeekers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Slice<JobSeeker> jobSeekers = jobSeekerRepository.findAll(pageable);

        List<JobSeeker> jobSeekerList = jobSeekers.getContent();

        List<JobSeekerListResponse> content = jobSeekerList.stream()
                .map(employer -> modelMapper.map(employer, JobSeekerListResponse.class))
                .toList();

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Success",
                JobSeekerResponsePage.builder()
                        .content(content)
                        .pageNo(jobSeekers.getNumber())
                        .pageSize(jobSeekers.getSize())
                        .totalElement(jobSeekers.getNumberOfElements())
                        .last(jobSeekers.isLast())
                        .build()
                )
        );
    }
}
