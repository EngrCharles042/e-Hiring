package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.domain.entities.JobSeekerVerificationToken;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.repositories.JobSeekerVerificationTokenRepository;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final JobSeekerVerificationTokenRepository tokenRepository;

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
}
