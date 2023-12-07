package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Report;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.*;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.enums.ExperienceLevel;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.ReportCat;
import com.swiftselect.infrastructure.event.events.JobPostCreatedEvent;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.jobpostrequests.*;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.jobpostresponse.*;
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
    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ResponseEntity<APIResponse<JobPostResponse>> createJobPost(JobPostRequest jobPostRequest) {

        Employer currentEmployer = getCurrentEmployerFromToken(request);

        List<Report> reports = reportRepository.findByJobPostEmployer(currentEmployer);

        if(reports.size()>=100){
            throw new ApplicationException("You are Blocked from posting because of excessive reports");
        }

        // Map the request to the JobPost entity
        JobPost jobPost = mapper.map(jobPostRequest, JobPost.class);

        jobPost.setEmployer(currentEmployer);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        addResponsibilitiesToJobPost(savedJobPost, jobPostRequest.getResponsibilities());
        addNiceToHaveToJobPost(savedJobPost, jobPostRequest.getNiceToHave());
        addQualificationsToJobPost(savedJobPost, jobPostRequest.getQualifications());

        JobPostResponse jobPostResponse = mapper.map(savedJobPost, JobPostResponse.class);
        jobPostResponse.setLogo(savedJobPost.getEmployer().getProfilePicture());
        jobPostResponse.setCompanyName(savedJobPost.getEmployer().getCompanyName());
        jobPostResponse.setCompanyId(savedJobPost.getEmployer().getId());

        applicationEventPublisher.publishEvent(new JobPostCreatedEvent(this, jobPost));

        return ResponseEntity.ok(new APIResponse<>("Job post created successfully", jobPostResponse));
    }

    private void addResponsibilitiesToJobPost(JobPost jobPost, List<String> responsibilities) {

        responsibilities.forEach(
                responsibility -> jobResponsibilitiesRepository.save(
                        JobResponsibilities.builder()
                                .responsibility(responsibility)
                                .jobPost(jobPost)
                                .build()
                )
        );
    }

    private void addQualificationsToJobPost(JobPost jobPost, List<String> qualifications) {

        qualifications.forEach(
                qualification -> qualificationRepository.save(
                        Qualification.builder()
                                .qualificationDetails(qualification)
                                .jobPost(jobPost)
                                .build()
                )
        );
    }

    private void addNiceToHaveToJobPost(JobPost jobPost, List<String> niceToHave) {

        niceToHave.forEach(
                nice2Have -> niceToHaveRepository.save(
                        NiceToHave.builder()
                                .niceToHave(nice2Have)
                                .jobPost(jobPost)
                                .build()
                )
        );
    }

    @Override
    public ResponseEntity<APIResponse<PostResponsePage>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Sort Condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Slice<JobPost> jobPosts = jobPostRepository.findAll(pageable);

        List<JobPost> jobPostList = jobPosts.getContent();

        List<JobPostResponse> content = listOfJobPostToListOfJobPostResponse(jobPostList);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Success",
                    PostResponsePage.builder()
                            .content(content)
                            .pageNo(jobPosts.getNumber())
                            .last(jobPosts.isLast())
                            .pageSize(jobPosts.getSize())
                            .totalElement(jobPosts.getNumberOfElements())
                            .build()
                )
        );
    }

    private List<ResponsibilityResponse> responsibilityConverter(List<JobResponsibilities> responsibilities) {
        return responsibilities.stream()
                .map(responsibility ->
                    ResponsibilityResponse.builder()
                            .id(responsibility.getId())
                            .responsibility(responsibility.getResponsibility())
                            .build())
                .toList();
    }

    private List<NiceToHaveResponse> niceToHaveConverter(List<NiceToHave> niceToHaves) {
        return niceToHaves.stream()
                .map(niceToHave ->
                        NiceToHaveResponse.builder()
                                .id(niceToHave.getId())
                                .niceToHave(niceToHave.getNiceToHave())
                                .build())
                .toList();
    }

    private List<QualificationResponse> qualificationConverter(List<Qualification> qualifications) {
        return qualifications.stream()
                .map(qualification ->
                        QualificationResponse.builder()
                                .id(qualification.getId())
                                .qualifications(qualification.getQualificationDetails())
                                .build())
                .toList();
    }

    private List<ApplicationResponse> applicationToApplicationResponse(List<Applications> applications) {
        return applications.stream()
                .map(application ->
                        ApplicationResponse.builder()
                                .id(application.getId())
                                .jobSeekerInfo(helperClass.jobSeekerToJobSeekerInfoResponse(application.getJobSeeker()))
                                .createDate(application.getCreateDate())
                                .build()
                ).toList();
    }

    @Override
    public ResponseEntity<APIResponse<JobPostResponse>> getJobPostById(Long id) {
        JobPost jobPost = jobPostRepository
                .findById(id)
                .orElseThrow(() -> new ApplicationException("Post not Found"));

        JobPostResponse jobPostResponse = mapper.map(jobPost, JobPostResponse.class);
        jobPostResponse.setCompanyName(jobPost.getEmployer().getCompanyName());
        jobPostResponse.setLogo(jobPost.getEmployer().getProfilePicture());

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Success",
                        jobPostResponse));
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

    public ResponseEntity<APIResponse<List<JobPostResponse>>> searchJobs(String query) {
        List<JobPost> jobPostSearch = jobPostRepository.searchJobs(query);

        List<JobPostResponse> searchResponses = jobPostSearch.stream()
                .map(jobPost -> {
                    JobPostResponse jobPostResponse = mapper.map(jobPost, JobPostResponse.class);
                    jobPostResponse.setCompanyName(jobPost.getEmployer().getCompanyName());
                    jobPostResponse.setLogo(jobPost.getEmployer().getProfilePicture());

                    return jobPostResponse;
                })
                .toList();


        return ResponseEntity.ok(new APIResponse<>("search completed",searchResponses));
    }

    @Override
    public ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPostByStateAndCountry(String query) {

        List<JobPost> jobPostSearch = jobPostRepository.findByStateAndCountry(query);

        List<JobPostResponse> searchResponses = jobPostSearch.stream()
                .map(jobPost -> {
                    JobPostResponse jobPostResponse = mapper.map(jobPost, JobPostResponse.class);
                    jobPostResponse.setCompanyName(jobPost.getEmployer().getCompanyName());
                    jobPostResponse.setLogo(jobPost.getEmployer().getProfilePicture());

                    return jobPostResponse;
                })
                .toList();

        return ResponseEntity.ok(new APIResponse<>("Success", searchResponses));
    }

    @Override
    public ResponseEntity<APIResponse<List<JobPostResponse>>> getJobPostByEmployerId(Long id) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        "success",
                        jobPostRepository.findJobPostsByEmployerId(id).stream()
                                .map(this::jobPostToJobPostResponse)
                                .toList()
                )
        );
    }

    @Override
    public ResponseEntity<APIResponse<List<JobPostResponse>>> findJobPostsByEmployer() {
        Employer employer = getCurrentEmployerFromToken(request);

        List<JobPost> jobPosts = jobPostRepository.findJobPostsByEmployer(employer);

        List<JobPostResponse> jobPostResponses = listOfJobPostToListOfJobPostResponse(jobPosts);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "success",
                        jobPostResponses
                )
        );
    }

    private List<JobPostResponse> listOfJobPostToListOfJobPostResponse(List<JobPost> jobPosts) {
        return jobPosts.stream()
                .map(this::jobPostToJobPostResponse)
                .toList();
    }

    public JobPostResponse jobPostToJobPostResponse(JobPost jobPost) {
        return JobPostResponse.builder()
                        .id(jobPost.getId())
                        .updateDate(jobPost.getUpdateDate())
                        .title(jobPost.getTitle())
                        .numOfPeopleToHire(jobPost.getNumOfPeopleToHire())
                        .description(jobPost.getDescription())
                        .country(jobPost.getCountry())
                        .state(jobPost.getState())
                        .employmentType(jobPost.getEmploymentType().toString())
                        .jobType(jobPost.getJobType().toString())
                        .applicationDeadline(jobPost.getApplicationDeadline().toString())
                        .jobCategory(jobPost.getJobCategory().toString())
                        .maximumPay(jobPost.getMaximumPay())
                        .minimumPay(jobPost.getMinimumPay())
                        .payRate(jobPost.getPayRate().toString())
                        .language(jobPost.getLanguage())
                        .yearsOfExp(jobPost.getYearsOfExp().toString())
                        .educationLevel(jobPost.getEducationLevel().toString())
                        .companyName(jobPost.getEmployer().getCompanyName())
                        .companyId(jobPost.getEmployer().getId())
                        .logo(jobPost.getEmployer().getProfilePicture())
                        .applications(applicationToApplicationResponse(jobApplicationRepository.findAllByJobPost(jobPost)))
                        .responsibilities(responsibilityConverter(jobResponsibilitiesRepository.findAllByJobPost(jobPost)))
                        .niceToHave(niceToHaveConverter(niceToHaveRepository.findAllByJobPost(jobPost)))
                        .qualifications(qualificationConverter(qualificationRepository.findAllByJobPost(jobPost)))
                        .build();
    }
}
