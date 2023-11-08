package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.EmployerService;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ForgotPasswordEventListener implements ApplicationListener<ForgotPasswordEvent> {
    private final EmailSenderService emailSenderService;
    private final EmployerService employerService;
    private final JobSeekerService jobSeekerService;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;

    private JobSeeker jobSeeker;
    private Employer employer;

    @Override
    public void onApplicationEvent(ForgotPasswordEvent event) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(event.getEmail());
        Optional<Employer> employerOptional = employerRepository.findByEmail(event.getEmail());

        if (jobSeekerOptional.isPresent()) {
            // Get the newly registered jobSeeker
            jobSeeker = jobSeekerOptional.get();

            // Build the verification url to be sent to the jobSeeker
            String url = event.getApplicationUrl() + "/auth/job-seeker/forgot-password/reset-page?email=" + event.getEmail();

            // Send the email to the jobSeeker
            emailSenderService.sendForgotPasswordEmailVerification(url, event);

            log.info("Click the link to verify your email and change ur password : {}", url);

        } else {
            // Get the newly registered jobSeeker
            employer = employerOptional.get();

            // Build the verification url to be sent to the jobSeeker
            String url = event.getApplicationUrl() + "/auth/job-seeker/forgot-password/reset-page?email=" + event.getEmail();

            // Send the email to the jobSeeker
            emailSenderService.sendForgotPasswordEmailVerification(url, event);

            log.info("Click the link to verify your email and change ur password : {}", url);
        }

    }
}
