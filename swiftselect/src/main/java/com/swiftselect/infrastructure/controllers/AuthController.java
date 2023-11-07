package com.swiftselect.infrastructure.controllers;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.payload.request.EmployerSignup;
import com.swiftselect.payload.request.JobSeekerSignup;
import com.swiftselect.payload.request.UserLogin;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/job-seeker/register")
    public ResponseEntity<JobSeeker> registerJobSeeker(@Valid @RequestBody JobSeekerSignup jobSeekerDto) {
        return authService.registerJobSeeker(jobSeekerDto);
    }

    @PostMapping("/employer/register")
    public ResponseEntity<Employer> registerEmployer(@Valid @RequestBody EmployerSignup employerSignup) {
        return authService.registerEmployer(employerSignup);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody UserLogin userLogin) {
        return authService.login(userLogin);
    }
}
