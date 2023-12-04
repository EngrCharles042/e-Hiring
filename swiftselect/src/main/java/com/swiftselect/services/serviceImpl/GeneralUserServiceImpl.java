package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import com.swiftselect.services.GeneralUserService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserServiceImpl implements GeneralUserService {
    private final HttpServletRequest request;
    private final FileUploadImpl fileUpload;
    private final JwtTokenProvider jwtTokenProvider;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final HelperClass helperClass;

    @Override
    public ResponseEntity<APIResponse<String>> uploadProfilePic(MultipartFile profilePic) {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtTokenProvider.getUserName(token);

        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);

        String fileUrl = null;

        try {
            if (jobSeekerOptional.isPresent()) {
                fileUrl = fileUpload.uploadFile(profilePic);

                JobSeeker jobSeeker = jobSeekerOptional.get();
                jobSeeker.setProfilePicture(fileUrl);

                jobSeekerRepository.save(jobSeeker);
            } else if (employerOptional.isPresent()) {
                fileUrl = fileUpload.uploadFile(profilePic);

                Employer employer = employerOptional.get();
                employer.setProfilePicture(fileUrl);

                employerRepository.save(employer);
            }
        } catch (IOException e) {
            throw new ApplicationException("File upload error, please try again");
        }
         return ResponseEntity.ok(
                 new APIResponse<>(
                         "successful",
                         fileUrl
                 )
         );
    }
}
