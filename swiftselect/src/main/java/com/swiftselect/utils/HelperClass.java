package com.swiftselect.utils;

import com.swiftselect.infrastructure.exceptions.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

@Component
public class HelperClass {

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        // Get the bearer token from the http request
        String bearerToken = request.getHeader("Authorization");

        // Extract only the Token excluding the prefix "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public void sendForgotPasswordEmail(String firstName, String url, JavaMailSender mailSender, String sendMail, String recipient) {
        try{
            String subject = "Email Verification";
            String senderName = "Swift Select Registration Portal Service";
            String mailContent =
                    "<p> Hi, " + firstName + ", </p>"
                            + "<p> Thank you for registering with us, <br>"
                            + "Please follow the link below to complete your registration. </p>"
                            + "<a href=" + url + "> Verify your email to activate your account </a> <br>"
                            + "<p> Thank you. <br> Swift Select Registration Portal Service </p>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, senderName);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
