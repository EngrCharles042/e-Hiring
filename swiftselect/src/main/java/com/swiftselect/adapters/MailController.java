package com.swiftselect.adapters;

import com.swiftselect.infrastructure.emailservice.EmailSenderService;
import com.swiftselect.userinterface.dtos.MailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<String> mailSender(@RequestBody MailDTO mailDTO) {
        emailSenderService.sendEmailAlert(mailDTO);

        return ResponseEntity.ok("Email sent successfully");
    }
}
