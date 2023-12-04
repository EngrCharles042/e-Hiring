package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.services.AuthService;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class ForgotPasswordEventListener implements ApplicationListener<ForgotPasswordEvent> {
    private final EmailSenderService emailSenderService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onApplicationEvent(ForgotPasswordEvent event) {
        // Create a verification token for the user
        String verificationToken = tokenProvider.generateValidationToken(event.getEmail(), SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        // Build the verification url to be sent to the jobSeeker
        String url = "http://127.0.0.1:1123/reset-forgot-password?token=" + verificationToken;

        // Send the email to the user
        emailSenderService.sendForgotPasswordEmailVerification(url, event);

        log.info("Click the link to verify your email and change ur password : {}", url);
    }
}
