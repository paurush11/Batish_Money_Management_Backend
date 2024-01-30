package com.example.batishMoneyManager.expenseData;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String dueDate;
	private Double amount;
	@Enumerated(EnumType.STRING)
	private Frequency frequency;
	@Enumerated(EnumType.STRING)
	private Category category;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	private Boolean reminders;
	private String notes;
	private Boolean automaticBillDetection;
	private Boolean alertsForPriceChanges;
}
