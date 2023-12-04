package com.swiftselect.infrastructure.event.events;

import com.swiftselect.domain.entities.jobpost.JobPost;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a new job post is created.
 */
@Getter
public class JobPostCreatedEvent extends ApplicationEvent {

    private final JobPost jobPost;

    /**
     * Constructs a new JobPostCreatedEvent.
     *
     * @param source   The source of the event.
     * @param jobPost  The JobPost associated with the event.
     */
    public JobPostCreatedEvent(Object source, final JobPost jobPost) {
        super(source);
        this.jobPost = jobPost;
    }
}


