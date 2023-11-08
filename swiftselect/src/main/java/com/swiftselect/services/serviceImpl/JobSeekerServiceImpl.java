package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.payload.request.ResetPasswordRequest;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.JobSeekerService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final PasswordEncoder passwordEncoder;
    private final JobSeekerRepository jobSeekerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final HelperClass helperClass;

    @Override
    public ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest) {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        JobSeeker jobSeeker = jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        jobSeeker.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        jobSeekerRepository.save(jobSeeker);

        emailSenderService.sendEmailAlert(
                new MailRequest(
                        email,
                        "Password Reset",
                        "Password successfully changed. \n If you did not initiate this, please send a reply to this email"
                )
        );

        return ResponseEntity.ok("Password successfully changed");
    }
}
