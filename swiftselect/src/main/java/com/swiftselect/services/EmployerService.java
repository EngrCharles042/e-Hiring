package com.swiftselect.services;

import com.swiftselect.domain.entities.Employer;

public interface EmployerService {
    void saveVerificationToken(Employer employer, String token);
    String validateToken(String token);
}
