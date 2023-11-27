package com.swiftselect.payload.response;

import com.swiftselect.domain.enums.Gender;
import com.swiftselect.domain.enums.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Role role;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
