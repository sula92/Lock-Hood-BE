package com.t6.lockhood.dto;

import com.t6.lockhood.model.Employee;
import com.t6.lockhood.model.enums.Priority;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskDTO {


    int id;
    String description;
    String completion;
    Date startingDate;
    Date finalDate;
    String priority;
    int employeeId;
}
