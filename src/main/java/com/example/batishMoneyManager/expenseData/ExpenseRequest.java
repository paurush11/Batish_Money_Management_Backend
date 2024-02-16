package com.example.batishMoneyManager.expenseData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
    private ExpenseData expenseData;
    private List<ExpenseUserSplits> userSplits;
    private Integer createdById;
}
