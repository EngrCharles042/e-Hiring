package com.swiftselect.payload.request.jobpostrequests;

import com.swiftselect.domain.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostRequest implements Serializable {
    @Size(min = 5, max = 250 , message = "Title should be between 5 and 25")
    @NotBlank(message = "required")
    private String title;

    @NotNull(message = "number of people to hire cannot be null")
    private Long numOfPeopleToHire;

    @Size(min = 15, max = 255 , message = "Description should be between 15 and 200")
    @NotBlank(message = "required")
    private String description;

    @Size(min = 2, max = 55 , message = "Country should be between 5 and 25")
    @NotBlank(message = "required")
    private String country;

    @Size(min = 2, max = 55 , message = "state should be between 5 and 25")
    @NotBlank(message = "required")
    private String state;

    private EmploymentType employmentType;

    private JobType jobType;

    private LocalDateTime applicationDeadline;

    private Industry jobCategory;

    @NotNull(message = "required")
    private Long maximumPay;

    @NotNull(message = "required")
    private Long minimumPay;

    private PayRate payRate;

    @NotBlank(message = "required")
    private String language;

    private YearsOfExp yearsOfExp;

    private ExperienceLevel experienceLevel;

    private EducationLevel educationLevel;

    private List<String> responsibilities = new ArrayList<>();

    private List<String> niceToHave = new ArrayList<>();

    private List<String> qualifications = new ArrayList<>();
}
