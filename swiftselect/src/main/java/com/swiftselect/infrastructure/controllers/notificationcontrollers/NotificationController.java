package com.swiftselect.infrastructure.controllers.notificationcontrollers;

import com.swiftselect.domain.entities.notification.Notification;
import com.swiftselect.payload.request.notificationRequest.SubscriptionRequest;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final JobSeekerService jobSeekerService;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToIndustry(@RequestBody SubscriptionRequest request) {
        jobSeekerService.subscribeJobSeekerToIndustry(request.getIndustries());
        return ResponseEntity.ok("Subscription successful");
    }

    @PatchMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        jobSeekerService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    public ResponseEntity<APIResponse<List<Notification>>> getNotifications(){
        return ResponseEntity.ok(jobSeekerService.getNotifications().getBody());
    }
}
