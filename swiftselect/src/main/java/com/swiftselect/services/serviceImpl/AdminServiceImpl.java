package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerResponsePage;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.AdminService;
import com.swiftselect.services.EmployerService;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final EmployerService employerService;
    private final JobSeekerService jobSeekerService;
    private final EmployerRepository employerRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<EmployerResponsePage> getAllEmployers(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Delegate the call to EmployerService's getAllEmployers method
        return employerService.getAllEmployers(pageNo, pageSize, sortBy, sortDir);
    }

    @Override
    public ResponseEntity<APIResponse<EmployerResponse>> getEmployerById(Long employerId) {
        Optional<Employer> employerOptional = employerRepository.findById(employerId);
        if (employerOptional.isEmpty()) {
            throw new ApplicationException("Employer With ID" + employerId + "Does Not Exist");
        }
        Employer employer = employerOptional.get();
        EmployerResponse employerResponse = modelMapper.map(employer, EmployerResponse.class);
        return ResponseEntity.ok(new APIResponse<>("Search Successful", employerResponse));
    }

    @Override
    public ResponseEntity<APIResponse<JobSeekerResponsePage>> getAllJobSeekers(int pageNo, int pageSize, String sortBy, String sortDir) {
        return jobSeekerService.getAllJobSeekers(pageNo, pageSize, sortBy, sortDir);
    }
    @Override
    public ResponseEntity<APIResponse<JobSeekerResponse>> getJobSeekerById(Long jobSeekerId) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findById(jobSeekerId);
        if (jobSeekerOptional.isEmpty()) {
            throw new ApplicationException("Job Seeker with ID " + jobSeekerId + " does not exist");
        }

        JobSeeker jobSeeker = jobSeekerOptional.get();
        JobSeekerResponse jobSeekerResponse = modelMapper.map(jobSeeker, JobSeekerResponse.class);

        return ResponseEntity.ok(new APIResponse<>("Job Seeker retrieved successfully", jobSeekerResponse));
    }

}
