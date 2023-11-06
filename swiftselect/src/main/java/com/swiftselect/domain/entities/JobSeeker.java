package com.swiftselect.domain.entities;

import com.swiftselect.domain.entities.base.Person;
import com.swiftselect.domain.enums.Availability;
import com.swiftselect.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String dob;

    private String skills;

    private String location;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "job_seekers_roles",
            joinColumns = @JoinColumn(name = "job_seeker_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Roles> roles = new HashSet<>();
}