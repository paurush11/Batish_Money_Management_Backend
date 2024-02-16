package com.example.batishMoneyManager.expenseData;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "expense", createIndex = true)
public class ExpenseData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer createdById;
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
	@ElementCollection
	private List<ExpenseUserSplits> userSplits;

}
