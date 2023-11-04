package com.swiftselect.domain.models.entities;

import com.swiftselect.domain.models.enums.ExpLvl;
import com.swiftselect.domain.models.enums.MinQual;
import com.swiftselect.domain.models.enums.YearsOfExp;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "qualification")
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MinQual minQualification;

    @Enumerated(value = EnumType.STRING)
    private ExpLvl expLevel;

    @Enumerated(value = EnumType.STRING)
    private YearsOfExp yearsOfExp;
}
