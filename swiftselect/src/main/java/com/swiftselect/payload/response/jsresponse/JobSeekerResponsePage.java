package com.swiftselect.payload.response.jsresponse;

import com.swiftselect.payload.response.employerresponse.EmployerListResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSeekerResponsePage {
    private List<JobSeekerListResponse> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean last;
}