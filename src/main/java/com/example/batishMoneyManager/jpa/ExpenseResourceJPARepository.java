package com.example.batishMoneyManager.jpa;

import java.util.List;
import java.util.Optional;

import com.example.batishMoneyManager.expenseData.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Id;
@EnableJpaRepositories
@Repository
public interface ExpenseResourceJPARepository extends JpaRepository<ExpenseData, Integer> {
	<S extends ExpenseData> S save(S entity);

	<S extends ExpenseData> List<S> saveAll(Iterable<S> entities);

	Optional<ExpenseData> findByid(Id id);

	List<ExpenseData> findAll();

	void delete(ExpenseData entity);

	void deleteAll();

	
	List<ExpenseData> findByCategory(Category category);

	// Find expenses by payment status
	List<ExpenseData> findByPaymentStatus(PaymentStatus paymentStatus);

	// Find expenses with amount greater than a specified value
	List<ExpenseData> findByAmountGreaterThan(Double amount);

	// Find expenses by frequency
	List<ExpenseData> findByFrequency(Frequency frequency);

	// Find expenses by payment method
	List<ExpenseData> findByPaymentMethod(PaymentMethod paymentMethod);

	// Find expenses with reminders set to true
	List<ExpenseData> findByRemindersTrue();

	Page<ExpenseData> findByCategory(Category category, Pageable pageable);

	Page<ExpenseData> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

	Page<ExpenseData> findByFrequency(Frequency frequency, Pageable pageable);

	Page<ExpenseData> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);

}
