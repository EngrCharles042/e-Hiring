package com.swiftselect.payload.response.employerresponse;

import com.swiftselect.domain.enums.CompanyType;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.domain.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerListResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String companyName;

    private String companyDescription;

    private String email;

    private Long numberOfEmployees;

    private String website;

    private String position;

    @Enumerated(value = EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(value = EnumType.STRING)
    private Industry industry;
}
