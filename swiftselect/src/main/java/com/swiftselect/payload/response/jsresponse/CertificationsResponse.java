package com.swiftselect.payload.response.jsresponse;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificationsResponse {
    private Long id;
    private String name;
    private String expiration;
}
