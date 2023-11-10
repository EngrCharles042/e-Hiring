package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.ResetPasswordRequest;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final AuthenticationManager authenticationManager;
    private final JobSeekerRepository jobSeekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HelperClass helperClass;
    private final EventPublisher eventPublisher;



    @Override
    public ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest) {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        // Authentication manager to authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        resetPasswordRequest.getOldPassword()
                )
        );

        JobSeeker jobSeeker = jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        jobSeeker.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        jobSeekerRepository.save(jobSeeker);

        eventPublisher.notificationMailEventPublisher(email,
                jobSeeker.getFirstName(), "Password Reset",
                "Password successfully changed. If you did not initiate this, please send a reply to this email",
                request);

        return ResponseEntity.ok("Password successfully changed");
    }
}
