package com.swiftselect.domain.models.entities;

import com.swiftselect.domain.models.entities.base.Base;
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

    private String description;

    private String jobSummary;

    private String location;

    private String employmentType;

    private LocalDateTime applicationDeadline;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_function_id")
    private JobFunction jobFunction;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<Applications> applications = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private Set<Report> reports = new HashSet<>();
}
