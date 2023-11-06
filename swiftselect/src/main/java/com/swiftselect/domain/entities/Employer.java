package com.swiftselect.domain.entities;

import com.swiftselect.domain.entities.base.Person;
import com.swiftselect.domain.enums.EmployerType;
import com.swiftselect.domain.enums.Position;
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

    private String email;

    private String notificationEmail;

    private boolean isEnabled;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employers_roles",
            joinColumns = @JoinColumn(name = "employer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Roles> roles = new HashSet<>();
}
