package com.swiftselect.payload.response.adminresponse;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignupResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
