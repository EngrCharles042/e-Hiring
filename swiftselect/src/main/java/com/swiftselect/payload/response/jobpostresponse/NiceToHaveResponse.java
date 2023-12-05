package com.swiftselect.payload.response.jobpostresponse;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NiceToHaveResponse {
    private Long id;
    private String niceToHave;
}
