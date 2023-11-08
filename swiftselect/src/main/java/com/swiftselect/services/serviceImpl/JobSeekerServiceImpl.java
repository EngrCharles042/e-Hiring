package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.domain.entities.JobSeekerVerificationToken;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.repositories.JobSeekerVerificationTokenRepository;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.payload.request.ResetPasswordRequest;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final JobSeekerVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final HelperClass helperClass;

    @Override
    public void saveVerificationToken(JobSeeker jobSeeker, String token) {
        JobSeekerVerificationToken verificationToken = JobSeekerVerificationToken.builder()
                .token(token)
                .jobSeeker(jobSeeker)
                .build();

        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String receivedToken) {
        Optional<JobSeekerVerificationToken> token = tokenRepository.findByToken(receivedToken);

        if (token.isEmpty()) {
            return "Invalid verification token";
        }

        JobSeeker jobSeeker = token.get().getJobSeeker();

        Calendar calendar = Calendar.getInstance();

        Long timeRemaining = token.get().getExpirationTime().getTime() - calendar.getTime().getTime();

        if (timeRemaining <= 0) {
            tokenRepository.delete(token.get());
            return "Token has Expired";
        } else {
            jobSeeker.setEnabled(true);
            jobSeekerRepository.save(jobSeeker);

            return "valid";
        }
    }

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

    @Override
    public ResponseEntity<String> jSChangePasswordPage(String email, ResetPasswordRequest passwordRequest) {
        JobSeeker employer = jobSeekerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        employer.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        jobSeekerRepository.save(employer);

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
