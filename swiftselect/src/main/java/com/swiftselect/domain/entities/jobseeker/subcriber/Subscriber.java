package com.swiftselect.domain.entities.jobseeker.subcriber;

import com.swiftselect.domain.entities.base.Base;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.enums.Industry;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subscribers")
public class Subscriber extends Base {
    @ManyToOne
    @JoinColumn(name = "jobseeker_Id")
    private JobSeeker jobSeeker;

    @Enumerated(EnumType.STRING)
    private Industry subscribedIndustry;
}
