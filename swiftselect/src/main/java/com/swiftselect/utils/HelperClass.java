package com.swiftselect.utils;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HelperClass {
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        // Get the bearer token from the http request
        String bearerToken = request.getHeader("Authorization");

        // Extract only the Token excluding the prefix "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            System.out.println(bearerToken);
            return bearerToken.substring(7);
        }

        return null;
    }

    public void sendEmail(
            String firstName,
            String url,
            JavaMailSender mailSender,
            String sendMail,
            String recipient,
            String action,
            String serviceProvider,
            String subject,
            String description
            ) {

        try {
            String mailContent ="<div style='padding: 1rem; background-color: rgb(138, 36, 36); color: white'>"
                    + "<p style='text-align: center'>"
                    + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                    + "<hr style='color: black'>"
                    + "<p> Hi, " + firstName + " </p>"
                    + "<p> " + description + " </p>"
                    + "<a href=" + url + " style='padding: 0.7rem; background-color: #383896; text-decoration: none; border-radius: 0.3rem; color: white'>" + action + "</a> <br>"
                    + "<p> Thank you. <br> " + serviceProvider + " </p>"
                    + "</div>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, serviceProvider);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public void sendNotificationEmail(
            String firstName,
            String url,
            JavaMailSender mailSender,
            String sendMail,
            String recipient,
            String action,
            String serviceProvider,
            String subject,
            String description
    ) {

        try {
            String mailContent ="<div style='padding: 1rem; background-color: white; color: black'>"
                    + "<p style='text-align: center'>"
                    + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                    + "<hr style='color: black'>"
                    + "<p> Hi, " + firstName + " </p>"
                    + "<p> " + description + " </p>"
                    + "<a href=" + url + " style='padding: 0.7rem; background-color: #383896; text-decoration: none; border-radius: 0.3rem; color: white'>" + action + "</a> <br>"
                    + "<p> Thank you. <br> " + serviceProvider + " </p>"
                    + "</div>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, serviceProvider);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public String emailVerification(
            String email,
            String action,
            String buttonName,
            String serviceProvider,
            String description
    ) {

        String firstName = extractFirstName(email);

        return "<head>"
                + "<title> " + action + " </title> "
                + "</head>"
                + "<body style='height: 100vh; overflow: hidden; margin: 0'>"
                + "<div style='padding: 2rem; background-color: white; color: gray; height: 100vh; width: 100vw; font-size: 20px; display: flex; justify-content: center; overflow: hidden'>"
                + "<div style='width: 35vw; color: black'>"
                + "<p style='text-align: center'>"
                + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                + "<hr style='color: black'>"
                + "<p style='font-family: Academy Engraved LET; font-size: 30px'> Hi, " + firstName + " </p>"
                + "<p style='font-family: Cochin; margin-bottom: 1.5rem'> " + description + " </p>"
                + "<a href='http://127.0.0.1:1123/login' style='font-family: Cochin; width: 35vw; height: 4rem; border-radius: 1rem; border: 1px solid saddlebrown; margin-top: 1rem; margin-bottom: 4rem; opacity: 0.8; background-color: rgb(36,36,138); color: white; font-size: 18px; cursor: pointer; padding: 1rem 2rem; text-align: center; text-decoration: none' />"
                + buttonName
                + "</a>"
                + "<p style='font-family: Cochin; margin-top: 1.5rem'> &copy; &nbsp;" + serviceProvider + " </p>"
                + "</div>"
                + "</div>"
                + "</body>";
    }

    public String extractFirstName(String email) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);

        if (jobSeekerOptional.isPresent()) {
            return jobSeekerOptional.get().getFirstName();
        }

        return employerOptional.get().getFirstName();
    }
}
