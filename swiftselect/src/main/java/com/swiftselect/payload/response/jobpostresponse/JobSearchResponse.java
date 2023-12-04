package com.swiftselect.payload.response.jobpostresponse;

import com.swiftselect.domain.enums.JobType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class JobSearchResponse {
    private String title;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType workMode;
    }


