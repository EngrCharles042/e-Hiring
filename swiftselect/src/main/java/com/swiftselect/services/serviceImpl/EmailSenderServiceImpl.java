package com.swiftselect.services.serviceImpl;

import com.swiftselect.services.EmailSenderService;
import com.swiftselect.payload.request.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;

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
}
