package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.infrastructure.event.events.EmployerCompleteRegistrationEvent;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.services.EmployerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployerCompleteRegistrationEventListener implements ApplicationListener<EmployerCompleteRegistrationEvent> {
    private final EmployerService employerService;
    private final EmailSenderService emailSenderService;
    private Employer employer;

    @Override
    public void onApplicationEvent(EmployerCompleteRegistrationEvent event) {
            // Get the newly registered employer
            employer = event.getEmployer();

            // Create a verification token for the user
            String verificationToken = UUID.randomUUID().toString();

            // Save the verification token for the user
            employerService.saveVerificationToken(employer, verificationToken);

            // Build the verification url to be sent to the employer
            String url = event.getApplicationUrl() + "/auth/employer/verify-email?token=" + verificationToken;

            // Send the email to the employer
            emailSenderService.sendVerificationEmailEmployer(url, employer);

            log.info("Click the link to verify your registration : {}", url);
        }
    }
