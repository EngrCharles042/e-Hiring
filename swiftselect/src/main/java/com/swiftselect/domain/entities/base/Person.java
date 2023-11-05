package com.swiftselect.domain.entities.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class Person extends Base {

    private String firstName;

    private String lastName;

    private String password;

    @Transient
    private String confirmPassword;

    private String phoneNumber;

    private String country;

    private String countryCode;
}
