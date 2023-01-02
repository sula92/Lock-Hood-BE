package com.t6.lockhood.model.pk;

import com.t6.lockhood.model.SuperEntity;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class RawMaterialPK extends SuperEntity {

    int rawMaterialId;
    int taskId;

}
