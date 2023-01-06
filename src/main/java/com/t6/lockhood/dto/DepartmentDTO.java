package com.t6.lockhood.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DepartmentDTO {

    int id;
    String name;
    int factoryId;
}
