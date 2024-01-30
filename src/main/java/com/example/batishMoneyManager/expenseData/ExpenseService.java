package com.example.batishMoneyManager.expenseData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
	private final ExpenseResourceRepository repository;
	
	public ExpenseService(ExpenseResourceRepository repository) {
		this.repository = repository;
	}

	public ExpenseData saveExpense(ExpenseData expense) {
		return repository.save(expense);
	}

	public List<ExpenseData> saveAllExpenses(Iterable<ExpenseData> expenses) {
		return repository.saveAll(expenses);
	}

	public Optional<ExpenseData> getExpenseById(Integer id) {
		return repository.findById(id);
	}

	public List<ExpenseData> getAllExpenses() {
		return repository.findAll();
	}

	public void deleteExpense(ExpenseData expense) {
		repository.delete(expense);
	}

	public void deleteAllExpenses() {
		repository.deleteAll();
	}


	public List<ExpenseData> getExpenseByCategory(Category category) {
		return repository.findByCategory(category);
	}

	public List<ExpenseData> getExpenseByFrequency(Frequency frequency) {
		return repository.findByFrequency(frequency);
	}

	public List<ExpenseData> getExpenseByPaymentStatus(PaymentStatus paymentStatus) {
		return repository.findByPaymentStatus(paymentStatus);
	}

	public List<ExpenseData> getExpenseByPaymentMethod(PaymentMethod paymentMethod) {
		return repository.findByPaymentMethod(paymentMethod);
	}

	public List<ExpenseData> getExpenseAmountGreaterThan(Double amount) {
		return repository.findByAmountGreaterThan(amount);
	}

	public List<ExpenseData> getExpenseByRemindersTrue() {
		return repository.findByRemindersTrue();
	}

	public Page<ExpenseData> getExpensesByCategoryWithPagination(Category category, Pageable pageable) {
		return repository.findByCategory(category, pageable);
	}

	public Page<ExpenseData> getExpensesByFrequencyWithPagination(Frequency frequency, Pageable pageable) {
		return repository.findByFrequency(frequency, pageable);
	}

	public Page<ExpenseData> getExpensesByPaymentStatusWithPagination(PaymentStatus paymentStatus, Pageable pageable) {
		return repository.findByPaymentStatus(paymentStatus, pageable);
	}

	public Page<ExpenseData> getExpensesByPaymentMethodWithPagination(PaymentMethod paymentMethod, Pageable pageable) {
		return repository.findByPaymentMethod(paymentMethod, pageable);
	}
//	private static List<ExpenseData> Expenses = new ArrayList<>();
//
//	private static int ExpenseCount = 0;
//	static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//
//	static {
//		Expenses.add(new ExpenseData("Shah-Palmer",
//				LocalDateTime.parse("2022-07-19T07:34:53.788636", formatter).toString(), 78.47, Frequency.monthly,
//				Category.rent, PaymentStatus.paid, PaymentMethod.bank_transfer, false, "", true, false, ""));
//		Expenses.add(new ExpenseData("Groceries", LocalDateTime.now().toString(), 150.00, Frequency.monthly,
//				Category.utilities, PaymentStatus.unpaid, PaymentMethod.cash, true, "Weekly groceries", false, true,
//				"Custom View"));
//	}

}
