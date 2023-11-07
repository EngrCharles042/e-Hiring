package com.swiftselect.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface JobSeekerService {
    public ResponseEntity<String> resetPassword(HttpServletRequest request, String newPassword);
}
