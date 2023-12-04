package com.swiftselect.payload.response.jobpostresponse;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private String id;
    private LocalDateTime createDate;
}
