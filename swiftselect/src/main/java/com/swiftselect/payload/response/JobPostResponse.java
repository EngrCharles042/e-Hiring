package com.swiftselect.payload.response;

import com.swiftselect.domain.entities.Report;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobpost.Applications;
import com.swiftselect.domain.entities.jobpost.JobResponsibilities;
import com.swiftselect.domain.entities.jobpost.NiceToHave;
import com.swiftselect.domain.entities.jobpost.Qualification;
import com.swiftselect.domain.enums.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostResponse {
    LocalDateTime updateDate;
    String title;
    Long numOfPeopleToHire;
    String description;
    String location;
    EmploymentType employmentType;
    JobType jobType;
    String applicationDeadline;
    Industry jobCategory;
    Long maximumPay;
    Long minimumPay;
    PayRate payRate;
    String language;
    YearsOfExp yearsOfExp;
    EducationLevel educationLevel;
    String howToApply;
    Set<NiceToHave> niceToHaveSet;
    Set<JobResponsibilities> responsibilities;
    Set<Qualification> qualifications;
    Employer employer;
    Set<Applications> applications;
    Set<Report> reports;
}