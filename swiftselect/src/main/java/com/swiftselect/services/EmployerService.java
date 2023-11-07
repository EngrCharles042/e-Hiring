package com.swiftselect.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface EmployerService {
   ResponseEntity<String> resetPassword(HttpServletRequest request, String newPassword);
}
