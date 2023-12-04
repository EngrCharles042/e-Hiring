package com.swiftselect.payload.request.jobpostrequests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponsibilitiesRequest {
    @NotBlank(message = "required")
    private String responsibility;
}
