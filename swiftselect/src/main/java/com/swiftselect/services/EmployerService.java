package com.swiftselect.services;

import com.swiftselect.payload.request.employerreqests.EmployerUpdateProfileRequest;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.employerresponse.EmployeeResponsePage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface EmployerService {
    ResponseEntity<APIResponse<String>> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest);
    ResponseEntity<APIResponse<String>> updateProfile(EmployerUpdateProfileRequest updateProfileRequest);
    ResponseEntity<APIResponse<String>> deleteJobPost(String email, Long postId);
    ResponseEntity<EmployeeResponsePage> getAllEmployers(int pageNo, int pageSize, String sortBy, String sortDir);
}
