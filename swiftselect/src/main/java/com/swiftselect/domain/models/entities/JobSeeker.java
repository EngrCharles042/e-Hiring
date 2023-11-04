package com.swiftselect.domain.models.entities;

import com.swiftselect.domain.models.entities.base.Person;
import com.swiftselect.domain.models.enums.Availability;
import com.swiftselect.domain.models.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

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

    private LocalDate DOB;

    private String skills;

    private String location;

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

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    private Set<Roles> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_function_id")
    private JobFunction jobFunction;
}
