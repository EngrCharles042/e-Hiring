package com.swiftselect.infrastructure.controllers;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.payload.request.EmployerSignup;
import com.swiftselect.payload.request.JobSeekerSignup;
import com.swiftselect.payload.request.UserLogin;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.services.AuthService;
import com.swiftselect.services.EmployerService;
import com.swiftselect.services.JobSeekerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JobSeekerService jobSeekerService;
    private final EmployerService employerService;
    private final AuthService authService;

    @PostMapping("/job-seeker/register")
    public ResponseEntity<JobSeeker> registerJobSeeker(@Valid @RequestBody JobSeekerSignup jobSeekerDto) {
        return authService.registerJobSeeker(jobSeekerDto);
    }

    @PostMapping("/employer/register")
    public ResponseEntity<Employer> registerEmployer(@Valid @RequestBody EmployerSignup employerSignup) {
        return authService.registerEmployer(employerSignup);
    }

    @GetMapping("/employer/verify-email")
    public ResponseEntity<String> employerVerifyToken(@RequestParam("token") String token) {
        String result = employerService.validateToken(token);

        if (result.equalsIgnoreCase("valid")) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(result);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid Verification Token");
    }

    @GetMapping("/job-seeker/verify-email")
    public ResponseEntity<String> jobSeekerVerifyToken(@RequestParam("token") String token) {
        String result = jobSeekerService.validateToken(token);

        if (result.equalsIgnoreCase("valid")) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(result);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid Verification Token");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody UserLogin userLogin) {
        return authService.login(userLogin);
    }
}
