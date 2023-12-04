package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.domain.entities.jobpost.JobPost;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.notification.Notification;
import com.swiftselect.domain.enums.Industry;
import com.swiftselect.infrastructure.event.events.JobPostCreatedEvent;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {
    private final JobSeekerService jobSeekerService;


    @EventListener
    public void handleJobPostCreatedEvent(JobPostCreatedEvent event) {
        JobPost jobPost = event.getJobPost();
        Industry jobType = jobPost.getJobCategory();
        List<JobSeeker> subscribers = jobSeekerService.getSubscribersByIndustry(jobType);

        log.info("Subscribers for {}: {}", jobType, subscribers);

        for (JobSeeker subscriber : subscribers) {
            String subject = "New job post in " + jobType + ": " + jobPost.getTitle();
            String description = "Check it out and apply now!\n\n" +
                    "Job Title: " + jobPost.getTitle() + "\n" +
                    "Description: " + jobPost.getDescription() + "\n" +
                    "Country: " + jobPost.getCountry() + "\n" +
                    "State: " + jobPost.getState() + "\n" +
                    "Application Deadline: " + jobPost.getApplicationDeadline();

            log.info("Sending notification email to {}: {}: {}", subscriber.getEmail(), subject,description);

            Notification notification = Notification.builder()
                    .recipient(subscriber)
                    .message(subject)
                    .read(false)
                    .relatedJobPost(jobPost)
                    .build();

            jobSeekerService.sendNotification(notification);
        }
    }
}

