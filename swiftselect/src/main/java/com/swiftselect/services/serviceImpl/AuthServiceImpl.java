package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.admin.Admin;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.enums.Gender;
import com.swiftselect.domain.enums.Role;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.adminrequest.AdminSignup;
import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.payload.response.adminresponse.AdminSignupResponse;
import com.swiftselect.payload.response.employerresponse.EmployerSignupResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerSignupResponse;
import com.swiftselect.repositories.*;
import com.swiftselect.services.AuthService;
import com.swiftselect.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher publisher;
    private final HttpServletRequest request;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(JobSeekerSignup jobSeekerSignup) {
        // Checks if a jobSeeker's email is already in the database
        boolean isPresent = jobSeekerRepository.existsByEmail(jobSeekerSignup.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist");
        }

        // Maps the jobSeekerSignup dto to a JobSeeker entity, so it can be saved
        JobSeeker newJobSeeker = modelMapper.map(jobSeekerSignup, JobSeeker.class);

        // Set Profile Pics
        if (jobSeekerSignup.getGender().equals(Gender.MALE)) {
            newJobSeeker.setProfilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png");
        } else {
            newJobSeeker.setProfilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/femaleProfile_yujimz.png");
        }

        // Assigning the role and isEnabled gotten to the newJobSeeker to be saved to the database
        newJobSeeker.setRole(Role.JOB_SEEKER);

        newJobSeeker.setEnabled(false);

        // Encrypt the password using Bcrypt password encoder
        newJobSeeker.setPassword(passwordEncoder.encode(jobSeekerSignup.getPassword()));

        // Save the jobSeeker to the database
        JobSeeker savedJobseeker = jobSeekerRepository.save(newJobSeeker);

        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedJobseeker.getEmail(), savedJobseeker.getFirstName(), request);

        JobSeekerSignupResponse signupResponse = modelMapper.map(savedJobseeker, JobSeekerSignupResponse.class);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>("Account created successfully", signupResponse)
        );
    }

    @Override
    public ResponseEntity<APIResponse<EmployerSignupResponse>> registerEmployer(EmployerSignup employerSignup) {
        // Checks if an Employer's email is already in the database
        boolean isPresent = employerRepository.existsByEmail(employerSignup.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("Employer with this e-mail already exist");
        }

        // Maps the EmployerSignup dto to an Employer entity, so it can be saved
        Employer newEmployer = modelMapper.map(employerSignup, Employer.class);

        // Set Profile Pics
        newEmployer.setProfilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png");

        // Assigning the role and isEnabled gotten to the newJobSeeker to be saved to the database
        newEmployer.setRole(Role.EMPLOYER);

        newEmployer.setEnabled(false);

        // Encrypt the password using Bcrypt password encoder
        newEmployer.setPassword(passwordEncoder.encode(employerSignup.getPassword()));

        // Assigning the roles and isEnabled gotten to the new Employer to be saved to the database
        Employer savedEmployer = employerRepository.save(newEmployer);

        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedEmployer.getEmail(), savedEmployer.getFirstName(), request);

        EmployerSignupResponse signupResponse = modelMapper.map(savedEmployer, EmployerSignupResponse.class);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<EmployerSignupResponse>("Account Created Successfully", signupResponse));
    }

    @Override
    public ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(AdminSignup adminSignup) {
        // Checks if an Employer's email is already in the database
        boolean isPresent = adminRepository.existsByEmail(adminSignup.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("Employer with this e-mail already exist");
        }

        // Maps the EmployerSignup dto to an Employer entity, so it can be saved
        Admin newAdmin = modelMapper.map(adminSignup, Admin.class);

        // Assigning the role and isEnabled gotten to the newJobSeeker to be saved to the database
        newAdmin.setRole(Role.ADMIN);

        newAdmin.setEnabled(false);

        // Encrypt the password using Bcrypt password encoder
        newAdmin.setPassword(passwordEncoder.encode(adminSignup.getPassword()));

        // Assigning the roles and isEnabled gotten to the new Employer to be saved to the database
        Admin savedAdmin = adminRepository.save(newAdmin);

        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedAdmin.getEmail(), savedAdmin.getFirstName(), request);

        AdminSignupResponse signupResponse = modelMapper.map(savedAdmin, AdminSignupResponse.class);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>("Account Created Successfully", signupResponse));
    }

    @Override
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(loginRequest.getEmail());
        Optional<Employer> employerOptional = employerRepository.findByEmail(loginRequest.getEmail());
        Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());

        if (jobSeekerOptional.isPresent() && !jobSeekerOptional.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new APIResponse<>("notVerified")
            );
        } else if (employerOptional.isPresent() && !employerOptional.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new APIResponse<>("notVerified")
            );
        } else if (adminOptional.isPresent() && !adminOptional.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new APIResponse<>("notVerified")
            );
        }

        // Authentication manager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

//      Saving authentication in security context so user won't have to login everytime the network is called
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // Generate jwt token
        String token = jwtTokenProvider.generateToken(authentication, SecurityConstants.JWT_EXPIRATION);

        // Generate jwt refresh token
        String refreshToken = jwtTokenProvider.generateToken(authentication, SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        if (jobSeekerOptional.isPresent()) {

            JobSeeker jobSeeker = jobSeekerOptional.get();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new APIResponse<>(
                                    "Login Successful",
                                    JwtAuthResponse.builder()
                                            .accessToken(token)
                                            .refreshToken(refreshToken)
                                            .tokenType("Bearer")
                                            .id(jobSeeker.getId())
                                            .email(jobSeeker.getEmail())
                                            .firstName(jobSeeker.getFirstName())
                                            .profilePicture(jobSeeker.getProfilePicture())
                                            .lastName(jobSeeker.getLastName())
                                            .gender(jobSeeker.getGender())
                                            .role(jobSeeker.getRole())
                                            .build()
                            )
                    );
        } else if (employerOptional.isPresent()) {
            Employer employer = employerOptional.get();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new APIResponse<>(
                                    "Login Successful",
                                    JwtAuthResponse.builder()
                                            .accessToken(token)
                                            .refreshToken(refreshToken)
                                            .tokenType("Bearer")
                                            .email(employer.getEmail())
                                            .id(employer.getId())
                                            .firstName(employer.getFirstName())
                                            .role(employer.getRole())
                                            .build()
                            )
                    );
        } else {
            Admin admin = adminOptional.get();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new APIResponse<>(
                                    "Login Successful",
                                    JwtAuthResponse.builder()
                                            .accessToken(token)
                                            .refreshToken(refreshToken)
                                            .tokenType("Bearer")
                                            .id(admin.getId())
                                            .email(admin.getEmail())
                                            .firstName(admin.getFirstName())
                                            .role(admin.getRole())
                                            .build()
                            )
                    );

        }
    }

    @Override
    public ResponseEntity<APIResponse<String>> resendVerificationEmail(LoginRequest loginRequest) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(loginRequest.getEmail());
        Optional<Employer> employerOptional = employerRepository.findByEmail(loginRequest.getEmail());
        Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());

        if (jobSeekerOptional.isEmpty() && employerOptional.isEmpty() && adminOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("User with this email does not exist"));
        }

        // If a user with the provided email exists, resend verification email
        String email;
        String firstName;

        if (jobSeekerOptional.isPresent()) {
            JobSeeker jobSeeker = jobSeekerOptional.get();
            email = jobSeeker.getEmail();
            firstName = jobSeeker.getFirstName();

        } else if (employerOptional.isPresent()) {
            Employer employer = employerOptional.get();
            email = employer.getEmail();
            firstName = employer.getFirstName();

        } else {
            Admin admin = adminOptional.get();
            email = admin.getEmail();
            firstName = admin.getFirstName();

        }
        // Send verification email logic here
        publisher.completeRegistrationEventPublisher(email, firstName, request);

        return ResponseEntity.ok(new APIResponse<>("Verification email has been resent to " + email));
    }

    @Override
    public ResponseEntity<APIResponse<String>> forgotPassword(String email) {
        if (!jobSeekerRepository.existsByEmail(email) && !employerRepository.existsByEmail(email) && !adminRepository.existsByEmail(email)) {
            throw new ApplicationException("Invalid email provided, please check and try again.");
        }

        publisher.forgotPasswordEventPublisher(email, request);

        return ResponseEntity.ok(new APIResponse<>("A link has been sent to your email to reset your password"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> validateToken(String receivedToken) {
        if (!jwtTokenProvider.validateToken(receivedToken)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("Token expired, please request for a new one", "expired"));
        }

        String email = jwtTokenProvider.getUserName(receivedToken);

        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        if (employerOptional.isPresent()) {
            Employer employer = employerOptional.get();

            if (employer.isEnabled()) {
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new APIResponse<>("This account has already been verified, please proceed to login", "already verified"));
            }

            employer.setEnabled(true);
            employerRepository.save(employer);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Verification Successful", "valid"));

        } else if (jobSeekerOptional.isPresent()) {
            JobSeeker jobSeeker = jobSeekerOptional.get();

            if (jobSeeker.isEnabled()) {
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new APIResponse<>("This account has already been verified, please proceed to login", "already verified"));
            }

            jobSeeker.setEnabled(true);
            jobSeekerRepository.save(jobSeeker);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Verification Successful", "valid"));

        } else if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            if (admin.isEnabled()) {
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new APIResponse<>("This account has already been verified, please proceed to login", "already verified"));
            }

            admin.setEnabled(true);
            adminRepository.save(admin);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Verification Successful", "valid"));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new APIResponse<>("invalid token", "invalid"));
    }

    public ResponseEntity<APIResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest) {
        if (!jwtTokenProvider.validateToken(forgotPasswordResetRequest.getToken())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("Token expired, please request for a new one"));
        }

        String email = jwtTokenProvider.getUserName(forgotPasswordResetRequest.getToken());

        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        if (employerOptional.isPresent()) {
            Employer employer = employerOptional.get();

            employer.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            employerRepository.save(employer);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Password Changed Successfully"));

        } else if (jobSeekerOptional.isPresent()) {
            JobSeeker jobSeeker = jobSeekerOptional.get();

            jobSeeker.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            jobSeekerRepository.save(jobSeeker);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Password Changed Successfully"));

        }else if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            admin.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            adminRepository.save(admin);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new APIResponse<>("Password Changed Successfully"));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new APIResponse<>("Invalid"));
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
