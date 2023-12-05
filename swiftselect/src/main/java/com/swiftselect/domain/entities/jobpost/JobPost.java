package com.swiftselect.domain.entities.jobpost;

import com.swiftselect.domain.entities.Report;
import com.swiftselect.domain.entities.base.Base;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_post")
public class JobPost extends Base {

    private String title;

    private Long numOfPeopleToHire;

    private String description;

    private String country;

    private String state;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime applicationDeadline;

    @Enumerated(EnumType.STRING)
    private Industry jobCategory;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    private Long maximumPay;

    private Long minimumPay;

    @Enumerated(EnumType.STRING)
    private PayRate payRate;

    private String language;

    @Enumerated(EnumType.STRING)
    private YearsOfExp yearsOfExp;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<NiceToHave> niceToHaveSet = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<JobResponsibilities> responsibilities = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<Qualification> qualifications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<Applications> applications = new HashSet<>();

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<Report> reports = new HashSet<>();
}
