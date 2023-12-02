package com.swiftselect.payload.request.notificationRequest;

import com.swiftselect.domain.enums.Industry;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {
    private Industry industry;
}
