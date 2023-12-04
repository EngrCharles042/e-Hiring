package com.swiftselect.services;

import com.swiftselect.payload.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {
    ResponseEntity<APIResponse<String>> uploadProfilePic(MultipartFile profilePic);
}
