package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.payload.request.EmployerUpdateProfileRequest;
import com.swiftselect.payload.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface EmployerService {
    ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);
    ResponseEntity<String> updateProfile(EmployerUpdateProfileRequest updateProfileRequest);
}
