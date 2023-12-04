package com.swiftselect.domain.entities.jobpost;

import com.swiftselect.domain.entities.base.Base;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nice_to_have")
public class NiceToHave extends Base {
    private String niceToHave;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "jobPost_id")
    private JobPost jobPost;
}
