package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Report;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import com.swiftselect.domain.entities.jobpost.Qualification;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.ReportCat;
import com.swiftselect.infrastructure.event.events.JobPostCreatedEvent;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.jobpostrequests.JobPostRequest;
import com.swiftselect.payload.request.jobpostrequests.JobResponsibilitiesRequest;
import com.swiftselect.payload.request.jobpostrequests.NiceToHaveRequest;
import com.swiftselect.payload.request.jobpostrequests.QualificationRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import com.swiftselect.payload.response.PostResponsePage;
import com.swiftselect.repositories.*;
import com.swiftselect.services.JobPostService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
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
    private final JobSeekerRepository jobSeekerRepository;
    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ResponseEntity<APIResponse<JobPostResponse>> createJobPost(JobPostRequest jobPostRequest) {


        Employer currentEmployer = getCurrentEmployerFromToken(request);

        List<Report> reports = reportRepository.findByJobPostEmployer(currentEmployer);
        if(reports.size()>=2){
            throw new ApplicationException("You are Blocked from posting because of excessive reports");
        }

        // Map the request to the JobPost entity
        JobPost jobPost = mapper.map(jobPostRequest, JobPost.class);

        jobPost.setEmployer(currentEmployer);

        jobPostRepository.save(jobPost);

        JobPostResponse jobPostResponse = mapper.map(jobPost, JobPostResponse.class);

        applicationEventPublisher.publishEvent(new JobPostCreatedEvent(this, jobPost));

        return ResponseEntity.ok(new APIResponse<>("Job post created successfully", jobPostResponse));
    }

    @Override
    public ResponseEntity<APIResponse<String>> addResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post"));

        responsibilitiesRequest.forEach(
                responsibilities -> {
                    JobResponsibilities responsibility = mapper.map(responsibilities, JobResponsibilities.class);
                    responsibility.setJobPost(jobPost);

                    jobResponsibilitiesRepository.save(responsibility);
                }
        );

        return ResponseEntity.ok(new APIResponse<>("Responsibilities added successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> addQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationReques) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post"));

        qualificationReques.forEach(
                qualification -> {
                    Qualification qualifications = mapper.map(qualification, Qualification.class);
                    qualifications.setJobPost(jobPost);
                    qualificationRepository.save(qualifications);
                }
        );

        return ResponseEntity.ok(new APIResponse<>("Qualifications added successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> addNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveReques) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository.findByIdAndEmployer(postId,currentEmployer)
                .orElseThrow(()-> new ApplicationException("You are not authorized to manage this job post"));

        niceToHaveReques.forEach(
                niceToHave -> {
                    NiceToHave niceToHaves = mapper.map(niceToHave, NiceToHave.class);
                    niceToHaves.setJobPost(jobPost);
                    niceToHaveRepository.save(niceToHaves);
                }
        );

        return ResponseEntity.ok(new APIResponse<>("NiceToHave added successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> updateJobPost(Long id, JobPostRequest jobPostRequest) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(id, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found"));

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

        return ResponseEntity.ok(new APIResponse<>("Update Successful"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> updateResponsibilitiesToJobPost(Long postId, Set<JobResponsibilitiesRequest> responsibilitiesRequest) {

        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found"));

        Set<JobResponsibilities> jobResponsibilities = jobPost.getResponsibilities();

        jobResponsibilitiesRepository.deleteAll(jobResponsibilities);

        return addResponsibilitiesToJobPost(postId, responsibilitiesRequest);
    }

    @Override
    public ResponseEntity<APIResponse<String>> updateQualificationToJobPost(Long postId, Set<QualificationRequest> qualificationReques) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found"));

        Set<Qualification> qualifications = jobPost.getQualifications();

        qualificationRepository.deleteAll(qualifications);

        return addQualificationToJobPost(postId, qualificationReques);
    }

    @Override
    public ResponseEntity<APIResponse<String>> updateNiceToHaveToJobPost(Long postId, Set<NiceToHaveRequest> niceToHaveReques) {
        Employer currentEmployer = getCurrentEmployerFromToken(request);

        JobPost jobPost = jobPostRepository
                .findByIdAndEmployer(postId, currentEmployer)
                .orElseThrow(() -> new ApplicationException("Post not Found"));

        Set<NiceToHave> niceToHaves = jobPost.getNiceToHaveSet();

        niceToHaveRepository.deleteAll(niceToHaves);

        return addNiceToHaveToJobPost(postId, niceToHaveReques);
    }

    @Override
    public ResponseEntity<APIResponse<PostResponsePage>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
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
                new APIResponse<>(
                        "Success",
                    PostResponsePage.builder()
                            .content(content)
                            .pageNo(jobPosts.getNumber())
                            .last(jobPosts.isLast())
                            .pageSize(jobPosts.getSize())
                            .totalElement(jobPosts.getTotalElements())
                            .totalPages(jobPosts.getTotalPages())
                            .build()
                )
        );
    }

    private Employer getCurrentEmployerFromToken(HttpServletRequest request) {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtTokenProvider.getUserName(token);
        return employerRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Invalid token or authentication issue"));
    }

    private JobSeeker getJobSeeker() {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        return  jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email));
    }

    @Override
    public ResponseEntity<APIResponse<String>> reportJobPost(Long jobId, String comment, ReportCat reportCategory) {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ApplicationException("Job post not found"));

        JobSeeker jobSeeker = getJobSeeker();

        Boolean reportExist = reportRepository.existsByJobSeekerId(jobSeeker.getId());

        if (reportExist){
            throw new ApplicationException("You can't report a post more than once. ");
        }

        Report report = new Report();

        report.setJobPost(jobPost);
        report.setJobSeeker(jobSeeker);
        report.setComment(comment);
        report.setReport_category(reportCategory);

        reportRepository.save(report);

        return ResponseEntity.ok(new APIResponse<>("Job post reported successfully"));
    }

    @Override
    public ResponseEntity<APIResponse<List<JobPost>>> getJobPostByJobType(JobType jobType) {

        List<JobPost> jobPosts = jobPostRepository.findAllByJobType(jobType);

        return ResponseEntity.ok(new APIResponse<>(jobPosts.toString()));
    }

    @Override
    public ResponseEntity<APIResponse<Slice<JobPostResponse>>> getJobPostByExperienceLevel(
            ExperienceLevel experienceLevel, int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Slice<JobPost> jobPostsSlice = jobPostRepository.findAllByExperienceLevel(experienceLevel, pageable);

        List<JobPostResponse> jobPostResponses = jobPostsSlice.getContent().stream()
                .map(jobPost -> mapper.map(jobPost, JobPostResponse.class))
                .collect(Collectors.toList());
        Slice<JobPostResponse> jobPostResponseSlice = new SliceImpl<>(
                jobPostResponses, pageable, jobPostsSlice.hasNext());

        return ResponseEntity.ok(new APIResponse<>("Job posts retrieved by experience level successfully", jobPostResponseSlice));
    }

    @Override
    public ResponseEntity<APIResponse<List<JobPost>>> searchJobPost(String query, JobType jobType, Industry jobCategory) {
        List<JobPost> allJobPosts = jobPostRepository.searchJobs(query, jobType, jobCategory);

        String queryLowerCase = query.toLowerCase();

        List<JobPost> suggestedJobPosts = allJobPosts.stream()
                .filter(jobPost ->
                        jobPost.getTitle().toLowerCase().contains(queryLowerCase) ||
                                jobPost.getJobType().toString().toLowerCase().contains(queryLowerCase) ||
                                jobPost.getJobCategory().toString().toLowerCase().contains(queryLowerCase) ||
                                jobPost.getEmployer().getCompanyName().toLowerCase().contains(queryLowerCase)
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(new APIResponse<>(suggestedJobPosts.toString()));
    }
}
