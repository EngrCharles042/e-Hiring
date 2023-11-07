package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.EmployerVerificationToken;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.EmployerVerificationTokenRepository;
import com.swiftselect.services.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final EmployerVerificationTokenRepository tokenRepository;
    private final EmployerRepository employerRepository;

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
}
