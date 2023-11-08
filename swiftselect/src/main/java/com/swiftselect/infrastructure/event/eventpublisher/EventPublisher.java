package com.swiftselect.infrastructure.event.eventpublisher;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.EmployerCompleteRegistrationEvent;
import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.infrastructure.event.events.JobSeekerCompleteRegistrationEvent;
import com.swiftselect.utils.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void jsRegistrationCompleteEventPublisher(JobSeeker jobSeeker, HttpServletRequest request) {
        eventPublisher.publishEvent(new JobSeekerCompleteRegistrationEvent(jobSeeker, AuthenticationUtils.applicationUrl(request)));
    }

    public void emRegistrationCompleteEventPublisher(Employer employer, HttpServletRequest request) {
        eventPublisher.publishEvent(new EmployerCompleteRegistrationEvent(employer, AuthenticationUtils.applicationUrl(request)));
    }

    public void forgotPasswordEventPublisher(String email, HttpServletRequest request) {
        eventPublisher.publishEvent(new ForgotPasswordEvent(email, AuthenticationUtils.applicationUrl(request)));
    }
}
