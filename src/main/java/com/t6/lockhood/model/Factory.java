package com.t6.lockhood.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
    int id;
    String name;
    @Basic
    @Temporal(TemporalType.DATE)
    Date date;

}
