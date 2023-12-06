package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.domain.enums.EducationLevel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducationResponse {
    private Long id;
    private String fieldOfStudy;
    private EducationLevel educationLevel;
    private String yearOfGraduation;
}
