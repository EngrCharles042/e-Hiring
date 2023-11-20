package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.employer.EmployerVerificationToken;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.JobSeekerVerificationToken;
import com.swiftselect.domain.enums.Role;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
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
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher publisher;
    private final HttpServletRequest request;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmployerVerificationTokenRepository employerTokenRepository;
    private final JobSeekerVerificationTokenRepository jobSeekerTokenRepository;
    private final JobSeekerVerificationTokenRepository jobSeekerVerificationTokenRepository;
    private final EmployerVerificationTokenRepository employerVerificationTokenRepository;

    @Override
    public ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(JobSeekerSignup jobSeekerSignup) {
        // Checks if a jobSeeker's email is already in the database
        boolean isPresent = jobSeekerRepository.existsByEmail(jobSeekerSignup.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        // Maps the jobSeekerSignup dto to a JobSeeker entity, so it can be saved
        JobSeeker newJobSeeker = modelMapper.map(jobSeekerSignup, JobSeeker.class);

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
            throw new ApplicationException("Employer with this e-mail already exist", HttpStatus.BAD_REQUEST);
        }

        // Maps the EmployerSignup dto to an Employer entity, so it can be saved
        Employer newEmployer = modelMapper.map(employerSignup, Employer.class);

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
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(loginRequest.getEmail());
        Optional<Employer> employerOptional = employerRepository.findByEmail(loginRequest.getEmail());

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
                                            .firstName(jobSeeker.getFirstName())
                                            .gender(jobSeeker.getGender())
                                            .role(jobSeeker.getRole())
                                            .build()
                            )
                    );
        } else {
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
                                            .id(employer.getId())
                                            .firstName(employer.getFirstName())
                                            .role(employer.getRole())
                                            .build()
                            )
                    );
        }
    }

    @Override
    public void saveVerificationToken(String email, String token) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);

        if (jobSeekerOptional.isPresent()) {
            JobSeekerVerificationToken verificationToken =
                    new JobSeekerVerificationToken(token, jobSeekerOptional.get());

            jobSeekerVerificationTokenRepository.save(verificationToken);
        } else {
            EmployerVerificationToken verificationToken =
                    new EmployerVerificationToken(token, employerOptional.get());

            employerVerificationTokenRepository.save(verificationToken);
        }
    }

    @Override
    public ResponseEntity<APIResponse<String>> forgotPassword(String email) {
        if (!jobSeekerRepository.existsByEmail(email) && !employerRepository.existsByEmail(email)) {
            throw new ApplicationException("Invalid email provided, please check and try again.",
                    HttpStatus.BAD_REQUEST);
        }

        Optional<JobSeekerVerificationToken> jobSeekerToken = jobSeekerTokenRepository.findByJobSeeker_Email(email);
        Optional<EmployerVerificationToken> employerToken = employerTokenRepository.findByEmployer_Email(email);

        jobSeekerToken.ifPresent(jobSeekerTokenRepository::delete);
        employerToken.ifPresent(employerTokenRepository::delete);

        publisher.forgotPasswordEventPublisher(email, request);

        return ResponseEntity.ok(new APIResponse<>("A link has been sent to your email to reset your password"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> validateToken(String receivedToken) {
        Optional<EmployerVerificationToken> employerToken = employerTokenRepository.findByToken(receivedToken);
        Optional<JobSeekerVerificationToken> jobSeekerToken = jobSeekerTokenRepository.findByToken(receivedToken);

        if (employerToken.isPresent()) {
            Employer employer = employerToken.get().getEmployer();

            if (employer.isEnabled()) {
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new APIResponse<>("This account has already been verified, please proceed to login"));
            }

            Calendar calendar = Calendar.getInstance();

            long timeRemaining = employerToken.get().getExpirationTime().getTime() - calendar.getTime().getTime();

            if (timeRemaining <= 0) {
                employerTokenRepository.delete(employerToken.get());

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new APIResponse<>("Expired"));
            } else {
                employer.setEnabled(true);
                employerRepository.save(employer);

                employerTokenRepository.delete(employerToken.get());

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(new APIResponse<>("Valid"));
            }

        } else if (jobSeekerToken.isPresent()) {
            JobSeeker jobSeeker = jobSeekerToken.get().getJobSeeker();

            if (jobSeeker.isEnabled()) {
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new APIResponse<>("This account has already been verified, please proceed to login"));
            }

            Calendar calendar = Calendar.getInstance();

            long timeRemaining = jobSeekerToken.get().getExpirationTime().getTime() - calendar.getTime().getTime();

            if (timeRemaining <= 0) {
                jobSeekerTokenRepository.delete(jobSeekerToken.get());

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new APIResponse<>("Expired"));
            } else {
                jobSeeker.setEnabled(true);
                jobSeekerRepository.save(jobSeeker);

                jobSeekerTokenRepository.delete(jobSeekerToken.get());

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(new APIResponse<>("Valid"));
            }
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new APIResponse<>("Invalid"));
    }

    @Override
    public ResponseEntity<APIResponse<String>> validateTokenForgotPassword(String receivedToken) {
        Optional<EmployerVerificationToken> employerToken = employerTokenRepository.findByToken(receivedToken);
        Optional<JobSeekerVerificationToken> jobSeekerToken = jobSeekerTokenRepository.findByToken(receivedToken);

        if (employerToken.isPresent()) {

            Calendar calendar = Calendar.getInstance();

            long timeRemaining = employerToken.get().getExpirationTime().getTime() - calendar.getTime().getTime();

            if (timeRemaining <= 0) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new APIResponse<>("Expired"));
            } else {

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(new APIResponse<>("Valid"));
            }

        } else if (jobSeekerToken.isPresent()) {

            Calendar calendar = Calendar.getInstance();

            long timeRemaining = jobSeekerToken.get().getExpirationTime().getTime() - calendar.getTime().getTime();

            if (timeRemaining <= 0) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new APIResponse<>("Expired"));
            } else {

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(new APIResponse<>("Valid"));
            }
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new APIResponse<>("Invalid"));
    }

    public ResponseEntity<APIResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest) {
        ResponseEntity<APIResponse<String>> result = validateTokenForgotPassword(forgotPasswordResetRequest.getToken());

       if (!Objects.requireNonNull(result.getBody()).getMessage().equals("Valid")) {
            throw new ApplicationException("Invalid Token", HttpStatus.BAD_REQUEST);
        }

        Optional<EmployerVerificationToken> employerToken = employerTokenRepository.findByToken(forgotPasswordResetRequest.getToken());
        Optional<JobSeekerVerificationToken> jobSeekerToken = jobSeekerTokenRepository.findByToken(forgotPasswordResetRequest.getToken());

        if (employerToken.isPresent()) {
            Employer employer = employerToken.get().getEmployer();

            employer.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            employerRepository.save(employer);

            employerTokenRepository.delete(employerToken.get());

        } else {
            JobSeeker jobSeeker = jobSeekerToken.get().getJobSeeker();

            jobSeeker.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            jobSeekerRepository.save(jobSeeker);

            jobSeekerTokenRepository.delete(jobSeekerToken.get());
        }

        APIResponse<String> response = new APIResponse<>("Password Changed Successfully");
        return ResponseEntity.ok(response);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
