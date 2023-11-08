package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.infrastructure.event.events.ForgotPasswordEvent;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.EmailSenderService;
import com.swiftselect.payload.request.MailRequest;
import com.swiftselect.utils.HelperClass;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final HelperClass helperClass;

    private JobSeeker jobSeeker;
    private Employer employer;

    @Value("${spring.mail.username}")
    private String sendMail;

    @Override
    public void sendEmailAlert(MailRequest mailDTO) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(sendMail);
            simpleMailMessage.setTo(mailDTO.getTo());
            simpleMailMessage.setSubject(mailDTO.getSubject());
            simpleMailMessage.setText(mailDTO.getMessage());

            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmailJobSeeker(String url, JobSeeker jobSeeker) {
        try{
            String subject = "Email Verification";
            String senderName = "Swift Select Registration Portal Service";
            String mailContent =
                    "<p> Hi, " + jobSeeker.getFirstName() + ", </p>"
                            + "<p> Thank you for registering with us, <br>"
                            + "Please follow the link below to complete your registration. </p>"
                            + "<a href=" + url + "> Verify your email to activate your account </a> <br>"
                            + "<p> Thank you. <br> Swift Select Registration Portal Service </p>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, senderName);
            messageHelper.setTo(jobSeeker.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public void sendVerificationEmailEmployer(String url, Employer employer) {
        try{
            String subject = "Email Verification";
            String senderName = "Swift Select Registration Portal Service";
            String mailContent =
                    "<p> Hi, " + employer.getFirstName() + ", </p>"
                            + "<p> Thank you for registering with us, <br>"
                            + "Please follow the link below to complete your registration. </p>"
                            + "<a href=" + url + "> Verify your email to activate your account </a> <br>"
                            + "<p> Thank you. <br> Swift Select Registration Portal Service </p>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, senderName);
            messageHelper.setTo(employer.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public void sendForgotPasswordEmailVerification(String url, ForgotPasswordEvent event) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(event.getEmail());
        Optional<Employer> employerOptional = employerRepository.findByEmail(event.getEmail());

        if (jobSeekerOptional.isPresent()) {
            jobSeeker = jobSeekerOptional.get();

            helperClass.sendForgotPasswordEmail(
                    jobSeeker.getFirstName(),
                    url,
                    mailSender,
                    sendMail,
                    event.getEmail());
        } else {
            employer = employerOptional.get();

            helperClass.sendForgotPasswordEmail(
                    employer.getFirstName(),
                    url,
                    mailSender,
                    sendMail,
                    event.getEmail());
        }
    }
}
