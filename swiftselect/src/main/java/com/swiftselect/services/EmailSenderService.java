package com.swiftselect.services;

import com.swiftselect.payload.request.MailRequest;

public interface EmailSenderService {
    void sendEmailAlert(MailRequest mailDTO);
}
