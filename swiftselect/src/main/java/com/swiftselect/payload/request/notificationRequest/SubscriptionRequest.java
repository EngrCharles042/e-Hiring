package com.swiftselect.payload.request.notificationRequest;

import com.swiftselect.domain.enums.Industry;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {

    private List<Industry> industries;
}
