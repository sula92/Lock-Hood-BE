package com.t6.lockhood.model;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String name;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    Department department;


}
