package com.swiftselect.payload.request;

import com.swiftselect.domain.enums.EmployerType;
import com.swiftselect.domain.enums.JobFunc;
import com.swiftselect.domain.enums.Position;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerSignup {
    @Size(min = 2, max = 25, message = "name must be at least 2 characters")
    @NotBlank(message = "name must not be blank")
    String firstName;

    @Size(min = 2, max = 25, message = "name must be at least 2 characters")
    @NotBlank(message = "name must not be blank")
    String lastName;

    @Size(min = 6, max = 15, message = "password must be at least 6 characters")
    @NotBlank(message = "password must not be blank")
    String password;

    @Size(min = 6, max = 15, message = "phone number must be at least 6 characters")
    @NotBlank(message = "Please input a valid number")
    String phoneNumber;

    @NotBlank(message = "Input valid country")
    String country;

    @NotBlank(message = "Input valid country code")
    String countryCode;

    @Size(min = 2, max = 25, message = "company name must be at least 2 characters")
    @NotBlank(message = "company name must not be blank")
    String companyName;

    @Size(min = 8, max = 50)
    @Email(message = "E-mail must be valid")
    @NotBlank(message = "E-mail required")
    String workEmail;

    @Size(min = 8, max = 50)
    @Email(message = "E-mail must be valid")
    @NotBlank(message = "E-mail required")
    String notificationEmail;

    @NotNull
    Position position;

    Long numberOfEmployees;

    String website;

    @Size(min = 3, max = 15, message = "address must be at least 3 characters")
    @NotBlank(message = "Address is required")
    String address;

    @NotNull(message = "Employer Type is required")
    EmployerType employerType;

    @Size(min = 3, max = 15, message = "contact person must be at least 3 characters")
    @NotBlank(message = "contact person is required")
    String contactPerson;

    @NotNull(message = "Job Function is required")
    JobFunc jobFunction;
}