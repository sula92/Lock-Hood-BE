package com.t6.lockhood.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TotIncomeAndExpensesDTO {

    private String grossIncome;
    private String netIncome;
    private String salariesPaid;
    private String totalOtherExpenses;

}
