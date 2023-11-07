package com.swiftselect.infrastructure.controllers;

import com.swiftselect.services.EmployerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request, @RequestParam("newPassword") String newPassword) {
        return employerService.resetPassword(request, newPassword);
    }
}
