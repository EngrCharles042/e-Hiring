package com.swiftselect.services;

import com.swiftselect.payload.request.adminrequest.AdminSignup;
import com.swiftselect.payload.request.authrequests.ForgotPasswordResetRequest;
import com.swiftselect.payload.request.authrequests.LoginRequest;
import com.swiftselect.payload.request.employerreqests.EmployerSignup;
import com.swiftselect.payload.request.jsrequests.JobSeekerSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.JwtAuthResponse;
import com.swiftselect.payload.response.adminresponse.AdminSignupResponse;
import com.swiftselect.payload.response.employerresponse.EmployerSignupResponse;
import com.swiftselect.payload.response.jsresponse.JobSeekerSignupResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<APIResponse<JobSeekerSignupResponse>> registerJobSeeker(JobSeekerSignup jobSeekerSignup);
    ResponseEntity<APIResponse<EmployerSignupResponse>> registerEmployer(EmployerSignup employerSignup);

    ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(AdminSignup adminSignup);

    ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest);
    void logout();

    ResponseEntity<APIResponse<String>> resendVerificationEmail(LoginRequest loginRequest);

    ResponseEntity<APIResponse<String>> forgotPassword(String email);
    ResponseEntity<APIResponse<String>> validateToken(String receivedToken);
    ResponseEntity<APIResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest);
}
