package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.EmployerVerificationToken;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.payload.request.ResetPasswordRequest;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.EmployerVerificationTokenRepository;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.EmployerService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final EmployerVerificationTokenRepository tokenRepository;
    private final EmployerRepository employerRepository;
    private final HelperClass helperClass;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Override
    public void saveVerificationToken(Employer employer, String token) {
        EmployerVerificationToken verificationToken = EmployerVerificationToken.builder()
                .token(token)
                .employer(employer)
                .build();

        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String receivedToken) {
        Optional<EmployerVerificationToken> token = tokenRepository.findByToken(receivedToken);

        if (token.isEmpty()) {
            return "Invalid verification token";
        }

        Employer employer = token.get().getEmployer();

        if (employer.isEnabled()) {
            return "This account has already been verified, please proceed to login";
        }

        Calendar calendar = Calendar.getInstance();

        Long timeRemaining = token.get().getExpirationTime().getTime() - calendar.getTime().getTime();

        if (timeRemaining <= 0) {
            tokenRepository.delete(token.get());
            return "Token has Expired";
        } else {
            employer.setEnabled(true);
            employerRepository.save(employer);

            return "valid";
        }
    }


    @Override
    public ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest) {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        Employer employer = employerRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        employer.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        employerRepository.save(employer);

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
    public ResponseEntity<String> eChangePasswordPage(String email, ResetPasswordRequest passwordRequest) {
        Employer employer = employerRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        employer.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        employerRepository.save(employer);

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
