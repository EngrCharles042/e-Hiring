package com.swiftselect.infrastructure.event.events;

import com.swiftselect.domain.entities.JobSeeker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class JobSeekerCompleteRegistrationEvent extends ApplicationEvent {
    private JobSeeker jobSeeker;
    private String applicationUrl;

    public JobSeekerCompleteRegistrationEvent(JobSeeker jobSeeker, String applicationUrl) {
        super(jobSeeker);
        this.jobSeeker = jobSeeker;
        this.applicationUrl = applicationUrl;
    }
}
