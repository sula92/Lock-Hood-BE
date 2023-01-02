package com.t6.lockhood.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class IncomeExpenseDTO {

    private String description;
    private String Date;
    private long Amount;

}
