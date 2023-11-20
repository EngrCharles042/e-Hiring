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
import com.swiftselect.payload.response.JobPostResponse;
import com.swiftselect.payload.response.PostResponsePage;
import com.swiftselect.repositories.*;
import com.swiftselect.services.JobPostService;
import com.swiftselect.utils.AppConstants;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public ResponseEntity<String> updateJobPost(Long id, JobPostRequest jobPostRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(id, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found", HttpStatus.NOT_FOUND));

        jobPost.setTitle(jobPostRequest.getTitle());
        jobPost.setNumOfPeopleToHire(jobPostRequest.getNumOfPeopleToHire());
        jobPost.setDescription(jobPostRequest.getDescription());
        jobPost.setLocation(jobPostRequest.getLocation());
        jobPost.setEmploymentType(jobPostRequest.getEmploymentType());
        jobPost.setJobType(jobPostRequest.getJobType());
        jobPost.setApplicationDeadline(jobPostRequest.getApplicationDeadline());
        jobPost.setJobCategory(jobPostRequest.getJobCategory());
        jobPost.setMaximumPay(jobPostRequest.getMaximumPay());
        jobPost.setMinimumPay(jobPostRequest.getMinimumPay());
        jobPost.setPayRate(jobPostRequest.getPayRate());
        jobPost.setLanguage(jobPostRequest.getLanguage());
        jobPost.setYearsOfExp(jobPostRequest.getYearsOfExp());
        jobPost.setEducationLevel(jobPostRequest.getEducationLevel());
        jobPost.setHowToApply(jobPostRequest.getHowToApply());

        jobPostRepository.save(jobPost);

        return ResponseEntity.ok("Update Successful");
    }

    @Override
    public ResponseEntity<String> updateResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found", HttpStatus.NOT_FOUND));

        Set<JobResponsibilities> jobResponsibilities = jobPost.getResponsibilities();

        jobResponsibilitiesRepository.deleteAll(jobResponsibilities);

        return addResponsibilitiesToJobPost(postId, responsibilitiesRequest);
    }

    @Override
    public ResponseEntity<String> updateQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found", HttpStatus.NOT_FOUND));

        Set<Qualification> qualifications = jobPost.getQualifications();

        qualificationRepository.deleteAll(qualifications);

        return addQualificationToJobPost(postId, qualificationRequest);
    }

    @Override
    public ResponseEntity<String> updateNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found", HttpStatus.NOT_FOUND));

        Set<NiceToHave> niceToHaves = jobPost.getNiceToHaveSet();

        niceToHaveRepository.deleteAll(niceToHaves);

        return addNiceToHaveToJobPost(postId, niceToHaveRequest);
    }

    @Override
    public ResponseEntity<PostResponsePage> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Sort Condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<JobPost> jobPosts = jobPostRepository.findAll(pageable);

        List<JobPost> jobPostList = jobPosts.getContent();

        List<JobPostResponse> content = jobPostList.stream()
                .map(jobPost -> mapper.map(jobPost, JobPostResponse.class))
                .toList();

        return ResponseEntity.ok(
                PostResponsePage.builder()
                        .content(content)
                        .pageNo(jobPosts.getNumber())
                        .last(jobPosts.isLast())
                        .pageSize(jobPosts.getSize())
                        .totalElement(jobPosts.getTotalElements())
                        .totalPages(jobPosts.getTotalPages())
                        .build()
        );
    }

    private Employer getCurrentEmployerFromToken(HttpServletRequest request) {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtTokenProvider.getUserName(token);
        return employerRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Invalid token or authentication issue",HttpStatus.FORBIDDEN));
    }
}
