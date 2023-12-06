package com.swiftselect.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ApplicationException extends RuntimeException {
    private final String customMessage;

    public ApplicationException(String customMessage) {
        super(customMessage);
        this.customMessage = customMessage;
    }

}
