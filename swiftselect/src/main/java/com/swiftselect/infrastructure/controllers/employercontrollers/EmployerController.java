package com.swiftselect.infrastructure.controllers.employercontrollers;

import com.swiftselect.payload.request.employerreqests.EmployerUpdateProfileRequest;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import com.swiftselect.services.EmployerService;
import com.swiftselect.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<String>> resetPassword(final HttpServletRequest request, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return employerService.resetPassword(request, resetPasswordRequest);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<APIResponse<String>> updateProfile(@RequestBody EmployerUpdateProfileRequest profileRequest) {
        return employerService.updateProfile(profileRequest);
    }

    @DeleteMapping("/delete-job-post/{post-id}")
    public ResponseEntity<APIResponse<String>> deleteJobPost(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable("post-id") Long postId) {
        return employerService.deleteJobPost(userDetails.getUsername(), postId);
    }

    @GetMapping
    public ResponseEntity<EmployerResponsePage> getAllEmployers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) String sortDir){
        return employerService.getAllEmployers(pageNo, pageSize, sortBy, sortDir);

    }
}
