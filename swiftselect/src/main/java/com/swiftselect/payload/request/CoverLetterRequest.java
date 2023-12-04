package com.swiftselect.payload.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverLetterRequest {
    private MultipartFile coverLetter;
}
