package com.swiftselect.payload.request.employerreqests;

import com.swiftselect.domain.enums.CompanyType;
import com.swiftselect.domain.enums.Industry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerUpdateProfileRequest {
    @Size(min = 2, max = 25, message = "company name must be at least 2 characters")
    @NotBlank(message = "company name must not be blank")
    String companyName;

    @Size(min = 10, max = 150, message = "company description must be at least 10 characters")
    @NotBlank(message = "company name must not be blank")
    String companyDescription;

    @Size(min = 3, max = 35, message = "address must be at least 3 characters")
    @NotBlank(message = "Address is required")
    String address;

    @Size(min = 3, max = 25, message = "country must be at least 3 characters")
    @NotBlank(message = "Country cannot be blank")
    String country;

    @Size(min = 3, max = 25, message = "state must be at least 3 characters")
    @NotBlank(message = "State cannot be empty")
    String state;

    @NotNull(message = "company type cannot be null")
    Industry industry;

    @NotNull(message = "company type cannot be null")
    CompanyType companyType;

    Long numberOfEmployees;

    String website;

    String facebook;

    String twitter;

    String instagram;
}
