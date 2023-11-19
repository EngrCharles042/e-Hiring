package com.swiftselect.infrastructure.controllers.authcontroller;

import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.payload.response.employerresponse.EmployerSignupResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerSignupResponse;
import com.swiftselect.services.AuthService;
import com.swiftselect.utils.AuthenticationUtils;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final HelperClass helperClass;

    @PostMapping("/job-seeker/register")
    public ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(@Valid @RequestBody JobSeekerSignup jobSeekerDto) {
        return authService.registerJobSeeker(jobSeekerDto);
    }

    @PostMapping("/employer/register")
    public ResponseEntity<APIResponse<EmployerSignupResponse>> registerEmployer(@Valid @RequestBody EmployerSignup employerSignup) {
        return authService.registerEmployer(employerSignup);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/register/verify-email")
    public ResponseEntity<APIResponse<String>> verifyToken(@RequestParam("token") String token) {

        return authService.validateToken(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<String>> forgotPassword(@RequestParam("email") String email) {
        return authService.forgotPassword(email);
    }

    @GetMapping(value = "/forgot-password/reset-password-page", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<APIResponse<String>> resetPasswordPage(@RequestParam("email") String email,
                                                    @RequestParam("token") String token,
                                                    final HttpServletRequest request) {

        ResponseEntity<APIResponse<String>> result = authService.validateTokenForgotPassword(token);


        if (!Objects.equals(Objects.requireNonNull(result.getBody()).getMessage(), "Valid")) {
            throw new ApplicationException(result.getBody().getMessage(), HttpStatus.FORBIDDEN);
        }

        String action = "SwiftSelect | Password Change";
        String serviceProvider = "Swift Select Customer Portal Service";
        String url = AuthenticationUtils.applicationUrl(request) + "/auth/success";
        String description = "Please provide the details below to change your password.";

        String htmlResponse = helperClass.restPasswordHtml(token, email, url, action, serviceProvider, description);

        // Create an APIResponse object with the HTML response
        APIResponse<String> response = new APIResponse<>("Success", htmlResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/success")
    public ResponseEntity<APIResponse<String>> success(@RequestParam("token") String token,
                                          @RequestParam("newPassword") String newPassword,
                                          @RequestParam("confirmNewPassword") String confirmNewPassword) {

        ForgotPasswordResetRequest forgotPasswordResetRequest = new ForgotPasswordResetRequest(token, newPassword, confirmNewPassword);

        return authService.resetForgotPassword(forgotPasswordResetRequest);
    }
}
