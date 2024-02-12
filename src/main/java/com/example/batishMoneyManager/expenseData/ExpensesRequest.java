package com.example.batishMoneyManager.expenseData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesRequest {
    private List<ExpenseData> expenses;
    private String userId;
}
