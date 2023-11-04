package com.swiftselect.domain.models.entities;

import com.swiftselect.domain.models.entities.base.Person;
import com.swiftselect.domain.models.enums.EmployerType;
import com.swiftselect.domain.models.enums.Position;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employer")
public class Employer extends Person {
    private String companyName;

    private String workEmail;

    private String notificationEmail;

    @Enumerated(value = EnumType.STRING)
    private Position position;

    private Long numberOfEmployees;

    private String website;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private EmployerType employerType;

    private String contactPerson;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private Set<JobPost> JobPosts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_function_id")
    private JobFunction jobFunction;

    @OneToMany(mappedBy ="employer", cascade = CascadeType.ALL)
    private Set<Roles> roles = new HashSet<>();
}
