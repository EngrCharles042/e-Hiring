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

    private String firstName;;

    private String lastName;

    private String companyName;

    private String companyDescription;

    private Long numberOfEmployees;

    private String website;

    private String position;

    @Enumerated(value = EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(value = EnumType.STRING)
    private Industry industry;
}
