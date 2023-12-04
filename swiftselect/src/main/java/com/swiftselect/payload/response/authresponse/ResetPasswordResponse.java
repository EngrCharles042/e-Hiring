package com.swiftselect.payload.response.authresponse;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordResponse {

    private Long id;
    private String firstName;
}
