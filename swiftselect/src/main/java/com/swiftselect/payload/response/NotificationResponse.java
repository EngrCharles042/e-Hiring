package com.swiftselect.payload.response;

import com.swiftselect.payload.response.jobpostresponse.JobPostResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private boolean read;
    private Long jobPostId;
    private String logo;
    private String companyName;
    private String createTime;
    private JobPostResponse jobPostResponse;
}
