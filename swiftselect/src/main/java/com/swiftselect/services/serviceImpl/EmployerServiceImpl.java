package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.EmployerService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final PasswordEncoder passwordEncoder;
    private final EmployerRepository employerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final HelperClass helperClass;

    @Override
    public ResponseEntity<String> resetPassword(HttpServletRequest request, String newPassword) {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        Employer employer = employerRepository
                .findByWorkEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        employer.setPassword(passwordEncoder.encode(newPassword));

        employerRepository.save(employer);

        emailSenderService.sendEmailAlert(
                new MailRequest(
                    email,
                    "Password Reset",
                     "Password successfully changed"
                )
        );

        return ResponseEntity.ok("Password successfully changed");
    }
}
