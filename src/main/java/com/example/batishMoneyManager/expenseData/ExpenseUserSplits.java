package com.example.batishMoneyManager.expenseData;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ExpenseUserSplits {
    private Integer userId;
    private Double splitAmount;
}
