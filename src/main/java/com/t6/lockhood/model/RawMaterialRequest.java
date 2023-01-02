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
public class RawMaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    Date date;
    int amount;
    int taskId;
}
