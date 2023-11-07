package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.JobSeekerCompleteRegistrationEvent;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSeekerCompleteRegistrationEventListener implements ApplicationListener<JobSeekerCompleteRegistrationEvent> {
    private final JobSeekerService jobSeekerService;
    private final EmailSenderService emailSenderService;
    private JobSeeker jobSeeker;

    @Override
    public void onApplicationEvent(JobSeekerCompleteRegistrationEvent event) {
        // Get the newly registered jobSeeker
        jobSeeker = event.getJobSeeker();

        // Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();

        // Save the verification token for the user
        jobSeekerService.saveVerificationToken(jobSeeker, verificationToken);

        // Build the verification url to be sent to the jobSeeker
        String url = event.getApplicationUrl() + "/auth/job-seeker/verify-email?token=" + verificationToken;

        // Send the email to the jobSeeker
        emailSenderService.sendVerificationEmailJobSeeker(url, jobSeeker);

        log.info("Click the link to verify your registration : {}", url);
    }
}
