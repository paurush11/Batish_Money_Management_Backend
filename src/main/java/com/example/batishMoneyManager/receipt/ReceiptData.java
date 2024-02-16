package com.example.batishMoneyManager.receipt;

import com.example.batishMoneyManager.expenseData.ExpenseData;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "receipt", createIndex = true)
public class ReceiptData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    private String startDate;
    private String endDate;
    private Double amount;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "receipt_expenses", joinColumns = @JoinColumn(name = "receipt_id"))
    @Column(name = "expense_id")
    private Set<Integer> expenseIds = new HashSet<>();
}
