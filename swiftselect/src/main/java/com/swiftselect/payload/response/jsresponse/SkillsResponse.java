package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.domain.enums.YearsOfExp;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillsResponse {
    private Long id;
    private String skill;
    private YearsOfExp yearsOfExperience;
}
