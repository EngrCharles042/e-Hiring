package com.swiftselect.infrastructure.controllers.generalcontrollers;

import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.services.GeneralUserService;
import com.swiftselect.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class GeneralUsersController {
    private final GeneralUserService generalUserService;

    @PutMapping("/profile-pic")
    public ResponseEntity<APIResponse<String>> resumeUpdate(@RequestParam MultipartFile profilePic) {

        if (profilePic.getSize() > AppConstants.MAX_FILE_SIZE) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>("FIle size exceed the normal limit"));
        }

        return generalUserService.uploadProfilePic(profilePic);
    }

}
