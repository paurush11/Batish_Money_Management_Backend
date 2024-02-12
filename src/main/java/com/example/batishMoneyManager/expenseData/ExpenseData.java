package com.example.batishMoneyManager.expenseData;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "expenses", createIndex = true)
public class ExpenseData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@org.springframework.data.annotation.Id
	private Integer id;
	private String name;
	private String dueDate;
	private Double amount;
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword)
	private Frequency frequency;
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword)
	private Category category;
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword)
	private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword)
	private PaymentMethod paymentMethod;
	private Boolean reminders;
	private String notes;
	private Boolean automaticBillDetection;
	private Boolean alertsForPriceChanges;
	private Integer userId;
}
