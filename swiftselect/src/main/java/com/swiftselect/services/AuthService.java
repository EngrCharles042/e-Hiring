package com.swiftselect.services;

import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.payload.response.employerresponse.EmployerSignupResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerSignupResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    void saveVerificationToken(String email, String token);
    ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(JobSeekerSignup jobSeekerSignup);
    ResponseEntity<APIResponse<EmployerSignupResponse>> registerEmployer(EmployerSignup employerSignup);
    ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest);
    void logout();
    ResponseEntity<APIResponse<String>> forgotPassword(String email);
    ResponseEntity<APIResponse<String>> validateToken(String receivedToken);
    ResponseEntity<APIResponse<String>> validateTokenForgotPassword(String receivedToken);
    ResponseEntity<APIResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest);
}
