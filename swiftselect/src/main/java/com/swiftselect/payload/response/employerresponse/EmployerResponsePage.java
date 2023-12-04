package com.swiftselect.payload.response.employerresponse;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerResponsePage {
        private List<EmployerResponse> content;
        private int pageNo;
        private int pageSize;
        private long totalElement;
        private int totalPages;
        private boolean last;
}
