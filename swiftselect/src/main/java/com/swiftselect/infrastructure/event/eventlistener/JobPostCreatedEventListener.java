package com.swiftselect.infrastructure.event.eventlistener;

import com.swiftselect.infrastructure.event.events.JobPostCreatedEvent;
import com.swiftselect.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JobPostCreatedEventListener {

    private final JobSeekerService jobSeekerService;


    @EventListener
    public void handleJobPostCreatedEvent(JobPostCreatedEvent event) {
        jobSeekerService.handleJobPostCreatedEvent(event);
    }
}

