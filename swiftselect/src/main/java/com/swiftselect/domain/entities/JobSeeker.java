package com.swiftselect.domain.entities;

import com.swiftselect.domain.entities.base.Person;
import com.swiftselect.domain.enums.Availability;
import com.swiftselect.domain.enums.Gender;
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
@Table(name = "job_seeker")
public class JobSeeker extends Person {
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String dob;

    private String skills;

    private boolean isEnabled;

    @Enumerated(value = EnumType.STRING)
    private Availability availability;

    private String CV;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    private Set<Applications> applications = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    private Set<Report> report = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role")
    private Roles role;
}