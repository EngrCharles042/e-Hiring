package com.swiftselect.services;

import com.swiftselect.payload.request.adminrequest.AdminSignup;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.adminresponse.AdminSignupResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<EmployerResponsePage> getAllEmployers(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<APIResponse<EmployerResponse>> getEmployerById(Long employerId);
}
