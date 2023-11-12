package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.infrastructure.event.eventpublisher.EventPublisher;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.infrastructure.security.JwtTokenProvider;
import com.swiftselect.payload.request.employerreqests.EmployerUpdateProfileRequest;
import com.swiftselect.payload.request.authrequests.ResetPasswordRequest;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.services.EmployerService;
import com.swiftselect.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final AuthenticationManager authenticationManager;
    private final EmployerRepository employerRepository;
    private final HelperClass helperClass;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;
    private final HttpServletRequest httpRequest;



    @Override
    public ResponseEntity<String> resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest) {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtTokenProvider.getUserName(token);

        // Authentication manager to authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        resetPasswordRequest.getOldPassword()
                )
        );

        Employer employer = employerRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new ApplicationException("User does not exist with email " + email, HttpStatus.NOT_FOUND));

        employer.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        employerRepository.save(employer);

        eventPublisher.notificationMailEventPublisher(email,
                employer.getFirstName(), "Password Reset",
                "Password successfully changed. If you did not initiate this, please send a reply to this email",
                request);

        return ResponseEntity.ok("Password successfully changed");
    }

    @Override
    public ResponseEntity<String> updateProfile(EmployerUpdateProfileRequest updateProfileRequest) {
        String token = helperClass.getTokenFromHttpRequest(httpRequest);

        String email = jwtTokenProvider.getUserName(token);

        Employer employer = employerRepository.findByEmail(email).get();

        employer.setCompanyName(updateProfileRequest.getCompanyName());
        employer.setCompanyDescription(updateProfileRequest.getCompanyDescription());
        employer.setAddress(updateProfileRequest.getAddress());
        employer.setCountry(updateProfileRequest.getCountry());
        employer.setState(updateProfileRequest.getState());
        employer.setCity(updateProfileRequest.getCity());
        employer.setIndustry(updateProfileRequest.getIndustry());
        employer.setCompanyType(updateProfileRequest.getCompanyType());
        employer.setNumberOfEmployees(updateProfileRequest.getNumberOfEmployees());
        employer.setWebsite(updateProfileRequest.getWebsite());
        employer.setFacebook(updateProfileRequest.getFacebook());
        employer.setTwitter(updateProfileRequest.getTwitter());
        employer.setInstagram(updateProfileRequest.getInstagram());
        employer.setFirstName(updateProfileRequest.getFirstName());
        employer.setLastName(updateProfileRequest.getLastName());
        employer.setPosition(updateProfileRequest.getPosition());
        employer.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        employer.setPostalCode(updateProfileRequest.getPostalCode());

        Employer savedEmployer = employerRepository.save(employer);

        return ResponseEntity.ok("Update Successful");
    }
}
