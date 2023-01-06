package com.t6.lockhood.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String incomeType;
    String description;
    int amount;
    Date date;
}
