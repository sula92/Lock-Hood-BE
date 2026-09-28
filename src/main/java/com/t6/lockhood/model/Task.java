package com.t6.lockhood.model;

import com.t6.lockhood.model.enums.Priority;
import lombok.*;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
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
