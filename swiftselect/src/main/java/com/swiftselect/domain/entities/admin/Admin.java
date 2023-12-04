package com.swiftselect.domain.entities.admin;

import com.swiftselect.domain.entities.base.Base;
import com.swiftselect.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "admins")
public class Admin extends Base {
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone",nullable = false)
    private String phoneNumber;

    private boolean isEnabled;

    private String country;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
