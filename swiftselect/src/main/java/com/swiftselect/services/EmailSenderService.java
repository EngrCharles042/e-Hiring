package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.payload.request.MailRequest;

public interface EmailSenderService {
    void sendEmailAlert(MailRequest mailDTO);
    void sendVerificationEmailJobSeeker(String url, JobSeeker jobSeeker);
    void sendVerificationEmailEmployer(String url, Employer employer);
    void sendForgotPasswordEmailVerification(String url, ForgotPasswordEvent event);
}
