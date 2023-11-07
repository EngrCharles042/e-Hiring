package com.swiftselect.infrastructure.event.events;

import com.swiftselect.domain.entities.Employer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmployerCompleteRegistrationEvent extends ApplicationEvent {
    private Employer employer;
    private String applicationUrl;

    public EmployerCompleteRegistrationEvent(Employer employer, String applicationUrl) {
        super(employer);
        this.employer = employer;
        this.applicationUrl = applicationUrl;
    }
}
