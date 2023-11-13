package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import com.swiftselect.domain.entities.jobpost.Qualification;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.jobPostRequests.JobPostRequest;
import com.swiftselect.payload.request.jobPostRequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobPostRequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobPostRequests.QualificationRequest;
import com.swiftselect.repositories.*;
import com.swiftselect.services.JobPostService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final ModelMapper mapper;
    private final JobPostRepository jobPostRepository;
    private final JobResponsibilitiesRepository jobResponsibilitiesRepository;
    private final QualificationRepository qualificationRepository;
    private final NiceToHaveRepository niceToHaveRepository;
    private final EmployerRepository employerRepository;
    private final HelperClass helperClass;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<String> createJobPost(JobPostRequest jobPostRequest) {

        Employer currentEmployer = getCurrentEmployerFromToken(request);

        // Map the request to the JobPost entity
        JobPost jobPost = mapper.map(jobPostRequest, JobPost.class);

        jobPost.setEmployer(currentEmployer);

        jobPostRepository.save(jobPost);

        return ResponseEntity.ok("Job post created successfully");
    }

    @Override
    public ResponseEntity<String> addResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post",HttpStatus.FORBIDDEN));

       responsibilitiesRequest.forEach(
                responsibilities -> {
                    JobResponsibilities responsibility = mapper.map(responsibilities, JobResponsibilities.class);
                    responsibility.setJobPost(jobPost);
                    jobResponsibilitiesRepository.save(responsibility);
                }
        );

        return ResponseEntity.ok("Responsibilities added successfully");
    }

    @Override
    public ResponseEntity<String> addQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post",HttpStatus.FORBIDDEN));

        qualificationRequest.forEach(
                qualification -> {
                    Qualification qualifications = mapper.map(qualification, Qualification.class);
                    qualifications.setJobPost(jobPost);
                    qualificationRepository.save(qualifications);
                }
        );

        return ResponseEntity.ok("Qualifications added successfully");
    }

    @Override
    public ResponseEntity<String> addNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post",HttpStatus.FORBIDDEN));

        niceToHaveRequest.forEach(
                niceToHave -> {
                    NiceToHave niceToHaves = mapper.map(niceToHave, NiceToHave.class);
                    niceToHaves.setJobPost(jobPost);
                    niceToHaveRepository.save(niceToHaves);
                }
        );

        return ResponseEntity.ok("NiceToHave added successfully");
    }

    private Employer getCurrentEmployerFromToken(HttpServletRequest request) {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtTokenProvider.getUserName(token);
        return employerRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Invalid token or authentication issue",HttpStatus.FORBIDDEN));
    }
}
