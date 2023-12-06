package com.swiftselect.payload.response.jobpostresponse;

import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import com.swiftselect.domain.entities.jobpost.Qualification;
import com.swiftselect.domain.enums.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostResponse {
    private Long id;
    private LocalDateTime updateDate;
    private String title;
    private Long numOfPeopleToHire;
    private String description;
    private String country;
    private String state;
    private String employmentType;
    private String jobType;
    private String applicationDeadline;
    private String jobCategory;
    private Long maximumPay;
    private Long minimumPay;
    private String payRate;
    private String language;
    private String yearsOfExp;
    private String educationLevel;
    private String companyName;
    private Long companyId;
    private String logo;
    private List<ApplicationResponse> applications = new ArrayList<>();
    private List<ResponsibilityResponse> responsibilities = new ArrayList<>();
    private List<NiceToHaveResponse> niceToHave = new ArrayList<>();
    private List<QualificationResponse> qualifications = new ArrayList<>();
}