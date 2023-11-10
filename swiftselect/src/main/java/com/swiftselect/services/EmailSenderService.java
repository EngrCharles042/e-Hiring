package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.payload.request.MailRequest;

public interface EmailSenderService {
    void sendEmailAlert(MailRequest mailDTO);
    void sendNotificationEmail(String url, String email, String firstName, String subject, String description);

    void sendRegistrationEmailVerification(String url, String email, String firstName);
    void sendForgotPasswordEmailVerification(String url, ForgotPasswordEvent event);
}
