package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.payload.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface EmployerService {
    void saveVerificationToken(Employer employer, String token);
    String validateToken(String token);
    ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);

}
