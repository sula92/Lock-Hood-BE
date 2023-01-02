package com.t6.lockhood.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EmployeeKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int efficiencyLevel;
    private int availabilityLevel;
    private  int reliabilityLevel;
    @OneToOne
    @JoinColumn(name = "employee", referencedColumnName = "id")
    Employee employee;
    int factoryId;
}
