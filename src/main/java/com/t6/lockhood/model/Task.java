package com.t6.lockhood.model;

import com.t6.lockhood.model.enums.Priority;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String description;
    String completion;
    Date startingDate;
    Date finalDate;
    @Enumerated(EnumType.STRING)
    Priority priority;
    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "id")
    Employee employees;


}
