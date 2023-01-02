package com.t6.lockhood.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskPriorityCountsDTO {

    int high;
    int medium;
    int low;
    int completed;
    int pending;

}
