package com.swiftselect.infrastructure.emailservice;

import com.swiftselect.userinterface.dtos.MailDTO;

public interface EmailSenderService {
    void sendEmailAlert(MailDTO mailDTO);
}
