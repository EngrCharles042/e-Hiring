package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.domain.entities.Roles;
import com.swiftselect.domain.enums.Role;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.payload.request.EmployerSignup;
import com.swiftselect.payload.request.JobSeekerSignup;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.repositories.RolesRepository;
import com.swiftselect.services.AuthService;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.utils.RandomTokenGen;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final ModelMapper modelMapper;
    private final EmailSenderService emailSenderService;
    private final RolesRepository rolesRepository;

    @Override
    public ResponseEntity<JobSeeker> registerJobSeeker(JobSeekerSignup jobSeekerSignup) {
        boolean isPresent = jobSeekerRepository.existsByEmail(jobSeekerSignup.getEmail());

        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<Roles> newRole = rolesRepository.findByName(Role.JOB_SEEKER);

        Set<Roles> roles = new HashSet<>();
        roles.add(newRole.get());

        JobSeeker newJobSeeker = modelMapper.map(jobSeekerSignup, JobSeeker.class);
        newJobSeeker.setRoles(roles);
        newJobSeeker.setEnabled(false);

        JobSeeker savedJobseeker = jobSeekerRepository.save(newJobSeeker);

        MailRequest mailRequest = MailRequest.builder()
                .to(savedJobseeker.getEmail())
                .subject("VERIFICATION TOKEN")
                .message(RandomTokenGen.generateRandomToken())
                .build();

        emailSenderService.sendEmailAlert(mailRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedJobseeker);
    }

    @Override
    public ResponseEntity<Employer> registerEmployer(EmployerSignup employerSignup) {
        boolean isPresent = employerRepository.existsByWorkEmail(employerSignup.getWorkEmail());

        if (isPresent) {
            throw new ApplicationException("Employer with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<Roles> newRole = rolesRepository.findByName(Role.EMPLOYER);

        Set<Roles> roles = new HashSet<>();
        roles.add(newRole.get());

        Employer newEmployer = modelMapper.map(employerSignup, Employer.class);
        newEmployer.setRoles(roles);
        newEmployer.setEnabled(false);

        Employer savedEmployer = employerRepository.save(newEmployer);

        MailRequest mailRequest = MailRequest.builder()
                .to(savedEmployer.getWorkEmail())
                .subject("VERIFICATION TOKEN")
                .message(RandomTokenGen.generateRandomToken())
                .build();

        emailSenderService.sendEmailAlert(mailRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployer);
    }
}
