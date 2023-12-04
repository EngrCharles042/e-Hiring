package com.swiftselect.payload.response.employerresponse;

import com.swiftselect.domain.enums.CompanyType;
import com.swiftselect.domain.enums.Industry;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String state;

    private String companyName;

    private String country;

    private String companyDescription;

    private String email;

    private Long numberOfEmployees;

    private String website;

    private String position;

    private String phoneNumber;

    private String postalCode;

    private String facebook;

    private String twitter;

    private String instagram;

    private String profilePicture;

    @Enumerated(value = EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(value = EnumType.STRING)
    private Industry industry;
}
