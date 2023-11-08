package com.swiftselect.services;

import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.payload.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface JobSeekerService {
    void saveVerificationToken(JobSeeker jobSeeker, String token);
    String validateToken(String token);
    ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);
}
