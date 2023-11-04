package com.swiftselect.domain.models.entities;

import com.swiftselect.domain.models.entities.base.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class Messaging extends Base {
    private String message;
}
