package com.swiftselect.payload.response.jobpostresponse;

import com.swiftselect.payload.response.jsresponse.JobSeekerInfoResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private Long id;
    private LocalDateTime createDate;
    private JobSeekerInfoResponse jobSeekerInfo;
}
