package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.domain.enums.YearsOfExp;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperienceResponse {
    private Long id;
    private String companyName;
    private String jobTitle;
    private LocalDate startDate;
    private LocalDate stopDate;
}
