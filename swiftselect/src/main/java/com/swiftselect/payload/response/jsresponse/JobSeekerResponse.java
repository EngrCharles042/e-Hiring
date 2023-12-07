package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.domain.enums.Gender;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Gender gender;

    private String facebook;

    private String instagram;

    private String twitter;
}
