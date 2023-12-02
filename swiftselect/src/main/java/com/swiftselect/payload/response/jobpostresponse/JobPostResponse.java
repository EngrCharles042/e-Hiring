package com.swiftselect.payload.response.jobpostresponse;

import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import com.swiftselect.domain.entities.jobpost.Qualification;
import com.swiftselect.domain.enums.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String location;
    private EmploymentType employmentType;
    private JobType jobType;
    private String applicationDeadline;
    private Industry jobCategory;
    private Long maximumPay;
    private Long minimumPay;
    private PayRate payRate;
    private String language;
    private YearsOfExp yearsOfExp;
    private EducationLevel educationLevel;
    private String howToApply;
    private String companyName;
    private String logo;
    private Set<JobResponsibilities> responsibilities = new HashSet<>();
    private Set<Qualification> qualifications = new HashSet<>();
    private Set<NiceToHave> niceToHaveSet = new HashSet<>();
}