package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.jobpost.Applications;
import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.jobpostresponse.JobApplicationResponse;
import com.swiftselect.repositories.JobApplicationRepository;
import com.swiftselect.repositories.JobPostRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.JobApplicationService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class JobApplicationImpl implements JobApplicationService {


    private final HelperClass helperClass;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;
    private final JobApplicationRepository applicationsRepository;
    private final ModelMapper modelMapper;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobPostRepository jobPostRepository;

    private JobSeeker getJobSeeker() {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        return jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email));
    }

    @Override
    public APIResponse<JobApplicationResponse> jobApplication(long jobPostId) {
        JobSeeker jobSeeker = getJobSeeker();

        Optional<JobPost> jobPostOptional = jobPostRepository.findById(jobPostId);

        if (jobPostOptional.isEmpty()) {
            throw new ApplicationException("Job Post not found");
        }
        Applications applications = Applications.builder()
                .jobPost(jobPostOptional.get())
                .jobSeeker(jobSeeker)
                .build();

        Applications savedApplication = applicationsRepository.save(applications);

        return  new APIResponse<>(
                "Application Successful",
                modelMapper.map(savedApplication, JobApplicationResponse.class)
                );

    }
}
