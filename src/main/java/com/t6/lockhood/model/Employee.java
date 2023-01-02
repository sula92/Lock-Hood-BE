package com.t6.lockhood.model;

import com.t6.lockhood.model.enums.CivilStatus;
import com.t6.lockhood.model.enums.EmployeeType;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String name;
    String address;
    String contact;
    @Enumerated(EnumType.STRING)
    private CivilStatus civilStatus;
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;
    String position;

    Date dateOfJoined;
    @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    Factory factory;
    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    Unit unit;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    Department department;



}
