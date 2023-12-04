package com.swiftselect.infrastructure.controllers.authcontroller;

import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.adminrequest.AdminSignup;
import com.swiftselect.payload.request.authrequests.ForgotPasswordRequest;
import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.payload.response.adminresponse.AdminSignupResponse;
import com.swiftselect.payload.response.employerresponse.EmployerSignupResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerSignupResponse;
import com.swiftselect.services.AuthService;
import com.swiftselect.utils.HelperClass;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/job-seeker/register")
    public ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(@Valid @RequestBody JobSeekerSignup jobSeekerDto) {
        return authService.registerJobSeeker(jobSeekerDto);
    }

    @PostMapping("/employer/register")
    public ResponseEntity<APIResponse<EmployerSignupResponse>> registerEmployer(@Valid @RequestBody EmployerSignup employerSignup) {
        return authService.registerEmployer(employerSignup);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(@Valid @RequestBody AdminSignup adminSignup) {
        return authService.registerAdmin(adminSignup);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping(value = "/register/verify-email", produces = MediaType.TEXT_HTML_VALUE)
    public String verifyToken(@RequestParam("token") String token) {
        ResponseEntity<APIResponse<String>> response = authService.validateToken(token);

        String email = tokenProvider.getUserName(token);
        String action = "SwiftSelect | Email Verification";
        String serviceProvider = "Swift Select Customer Portal Service";
        String description = Objects.requireNonNull(response.getBody()).getMessage();

        String htmlResponse = helperClass.emailVerification(email, action, "Go to Login Page", serviceProvider, description);



        String state = Objects.requireNonNull(response.getBody()).getData();

        if (state.equals("valid")) {
            return htmlResponse;
        }

        return "invalid";
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<APIResponse<String>> resendVerificationEmail(@RequestBody LoginRequest loginRequest) {
        return authService.resendVerificationEmail(loginRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest.getEmail());
    }

    @PostMapping(value = "/reset-forgot-password")
    public ResponseEntity<APIResponse<String>> resetForgotPassword(@Valid @RequestBody ForgotPasswordResetRequest forgotPasswordResetRequest) {

        return authService.resetForgotPassword(forgotPasswordResetRequest);
    }

    @GetMapping("/logout")
    private ResponseEntity<APIResponse<String>> logout(){
        authService.logout();
        return ResponseEntity.ok(new APIResponse<>("Logout Successfully"));
    }
}



























//    @GetMapping(value = "/forgot-password/reset-password-page", produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<APIResponse<String>> resetPasswordPage(@RequestParam("email") String email,
//                                                    @RequestParam("token") String token,
//                                                    final HttpServletRequest request) {
//
//        ResponseEntity<APIResponse<String>> result = authService.validateTokenForgotPassword(token);
//
//
//        if (!Objects.equals(Objects.requireNonNull(result.getBody()).getMessage(), "Valid")) {
//            throw new ApplicationException(result.getBody().getMessage());
//        }
//
//        String action = "SwiftSelect | Email Verification";
//        String serviceProvider = "Swift Select Customer Portal Service";
//        String url = AuthenticationUtils.applicationUrl(request) + "/auth/success";
//        String description = "Please provide the details below to change your password.";
//
//        String htmlResponse = helperClass.restPasswordHtml(token, email, url, action, serviceProvider, description);
//
//        // Create an APIResponse object with the HTML response
//        APIResponse<String> response = new APIResponse<>("Success", htmlResponse);
//
//        return ResponseEntity.ok(response);
//    }