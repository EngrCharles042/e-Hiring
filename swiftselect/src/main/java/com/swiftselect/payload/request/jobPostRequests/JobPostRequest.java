package com.swiftselect.payload.request.jobPostRequests;

import com.swiftselect.domain.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostRequest {
    @Size(min = 5, max = 25 , message = "Title should be between 5 and 25")
    @NotBlank(message = "required")
    private String title;

    @NotNull(message = "number of people to hire cannot be null")
    private Long numOfPeopleToHire;

    @Size(min = 15, max = 200 , message = "Description should be between 15 and 200")
    @NotBlank(message = "required")
    private String description;

    @Size(min = 5, max = 25 , message = "Location should be between 5 and 25")
    @NotBlank(message = "required")
    private String location;

    @NotNull(message = "required")
    private EmploymentType employmentType;

    @NotNull(message = "required")
    private JobType jobType;

    @NotBlank(message = "required")
    private String applicationDeadline;

    @NotNull(message = "required")
    private Industry jobCategory;

    @NotNull(message = "required")
    private Long maximumPay;

    @NotNull(message = "required")
    private Long minimumPay;

    @NotNull(message = "required")
    private PayRate payRate;

    @NotBlank(message = "required")
    private String language;

    @NotNull(message = "required")
    private YearsOfExp yearsOfExp;

    @NotNull(message = "required")
    private EducationLevel educationLevel;

    @Size(min = 15, max = 200 , message = "Application description should be between 15 and 200")
    @NotBlank(message = "required")
    private String howToApply;
}