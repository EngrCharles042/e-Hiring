package com.swiftselect.payload.response.employerresponse;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponsePage {

        private List<EmployerListResponse> content;
        private int pageNo;
        private int pageSize;
        private long totalElement;
        private int totalPages;
        private boolean last;

}
