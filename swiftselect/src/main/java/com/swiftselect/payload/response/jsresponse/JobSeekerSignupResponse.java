package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.domain.enums.Gender;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerSignupResponse {
    Long id;

    String firstName;

    String lastName;

    String email;

    Gender gender;
}
