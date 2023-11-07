package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.domain.entities.Roles;
import com.swiftselect.domain.enums.Role;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher publisher;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<JobSeeker> registerJobSeeker(JobSeekerSignup jobSeekerSignup) {
        // Checks if a jobSeeker's email is already in the database
        boolean isPresent = jobSeekerRepository.existsByEmail(jobSeekerSignup.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        // Get the instance of the role to be assigned to the jobSeeker
        Optional<Roles> newRole = rolesRepository.findByName(Role.JOB_SEEKER);

        // Makes the instance of the gotten role a set of roles
        Set<Roles> roles = new HashSet<>();
        roles.add(newRole.get());

        // Maps the jobSeekerSignup dto to a JobSeeker entity, so it can be saved
        JobSeeker newJobSeeker = modelMapper.map(jobSeekerSignup, JobSeeker.class);

        // Assigning the roles and isEnabled gotten to the newJobSeeker to be saved to the database
        newJobSeeker.setRoles(roles);
        newJobSeeker.setEnabled(false);

        // Encrypt the password using Bcrypt password encoder
        newJobSeeker.setPassword(passwordEncoder.encode(jobSeekerSignup.getPassword()));

        // Save the jobSeeker to the database
        JobSeeker savedJobseeker = jobSeekerRepository.save(newJobSeeker);

        // Publish and event to verify Email
        publisher.jsRegistrationCompleteEventPublisher(savedJobseeker, request);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJobseeker);
    }

    @Override
    public ResponseEntity<Employer> registerEmployer(EmployerSignup employerSignup) {
        // Checks if an Employer's email is already in the database
        boolean isPresent = employerRepository.existsByEmail(employerSignup.getWorkEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("Employer with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        // Get the instance of the role to be assigned to the Employer
        Optional<Roles> newRole = rolesRepository.findByName(Role.EMPLOYER);

        // Makes the instance of the gotten role a set of roles
        Set<Roles> roles = new HashSet<>();
        roles.add(newRole.get());

        // Maps the EmployerSignup dto to an Employer entity, so it can be saved
        Employer newEmployer = modelMapper.map(employerSignup, Employer.class);
        newEmployer.setRoles(roles);
        newEmployer.setEnabled(false);

        // Encrypt the password using Bcrypt password encoder
        newEmployer.setPassword(passwordEncoder.encode(employerSignup.getPassword()));

        // Assigning the roles and isEnabled gotten to the new Employer to be saved to the database
        Employer savedEmployer = employerRepository.save(newEmployer);

        // Publish and event to verify Email
        publisher.emRegistrationCompleteEventPublisher(savedEmployer, request);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployer);
    }
}
