package com.t6.lockhood.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UnitDTO {

    int id;
    String name;
    int departmentId;
}
