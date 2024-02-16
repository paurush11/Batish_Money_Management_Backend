package com.example.batishMoneyManager.expenseData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.batishMoneyManager.Exceptions.CustomException;
import com.example.batishMoneyManager.Exceptions.CustomUserNotFoundException;
import com.example.batishMoneyManager.User.User;
import com.example.batishMoneyManager.User.UserService;
import com.example.batishMoneyManager.elasticSearch.ExpenseResourceElasticSearchRepository;
import com.example.batishMoneyManager.jpa.ExpenseResourceJPARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService {
	private final ExpenseResourceJPARepository jpaRepository;
	private final UserService userService;
	private final ExpenseResourceElasticSearchRepository elasticSearchRepository;
	
	public ExpenseService(ExpenseResourceJPARepository jpaRepository, UserService userService, ExpenseResourceElasticSearchRepository elasticSearchRepository) {
		this.jpaRepository = jpaRepository;
        this.userService = userService;
        this.elasticSearchRepository = elasticSearchRepository;
	}

	public ExpenseData saveExpense(ExpenseData expense) {
		ExpenseData savedExpense = jpaRepository.save(expense);
		elasticSearchRepository.save(expense);
		return savedExpense;
	}

	public List<ExpenseData> saveAllExpenses(Iterable<ExpenseData> expenses) {
		List<ExpenseData> savedExpenseList = jpaRepository.saveAll(expenses);
		elasticSearchRepository.saveAll(expenses);
		return savedExpenseList;
	}

	public Optional<ExpenseData> getExpenseById(Integer id) {
		Optional<ExpenseData> savedExpense = jpaRepository.findById(id);
		return savedExpense;
	}

	public List<ExpenseData> getAllExpenses() {
		List<ExpenseData> allExpenses = jpaRepository.findAll();
		return allExpenses;
	}

	private void deleteExpense(ExpenseData expense) {
		jpaRepository.delete(expense);
		elasticSearchRepository.delete(expense);
	}

	public void deleteAllExpenses() {
		jpaRepository.deleteAll();
	}


	public List<ExpenseData> getExpenseByCategory(Category category) {
		List<ExpenseData> savedExpenses = jpaRepository.findByCategory(category);
		return savedExpenses;
	}

	public List<ExpenseData> getExpenseByFrequency(Frequency frequency) {
		List<ExpenseData> savedExpenses = jpaRepository.findByFrequency(frequency);
		return savedExpenses;
	}

	public List<ExpenseData> getExpenseByPaymentStatus(PaymentStatus paymentStatus) {
		List<ExpenseData> savedExpenses = jpaRepository.findByPaymentStatus(paymentStatus);
		return savedExpenses;
	}

	public List<ExpenseData> getExpenseByPaymentMethod(PaymentMethod paymentMethod) {
		List<ExpenseData> savedExpenses = jpaRepository.findByPaymentMethod(paymentMethod);
		return savedExpenses;
	}

	public List<ExpenseData> getExpenseAmountGreaterThan(Double amount) {
		List<ExpenseData> savedExpenses = jpaRepository.findByAmountGreaterThan(amount);
		return savedExpenses;
	}

	public List<ExpenseData> getExpenseByRemindersTrue() {
		List<ExpenseData> savedExpenses = jpaRepository.findByRemindersTrue();
		return savedExpenses;
	}

	public Page<ExpenseData> getExpensesByCategoryWithPagination(Category category, Pageable pageable) {
		Page<ExpenseData> savedExpenses = elasticSearchRepository.findByCategory(category, pageable);
		return savedExpenses;
	}

	public Page<ExpenseData> getExpensesByFrequencyWithPagination(Frequency frequency, Pageable pageable) {
		Page<ExpenseData> savedExpenses = elasticSearchRepository.findByFrequency(frequency, pageable);
		return savedExpenses;
	}

	public Page<ExpenseData> getExpensesByPaymentStatusWithPagination(PaymentStatus paymentStatus, Pageable pageable) {
		Page<ExpenseData> savedExpenses = elasticSearchRepository.findByPaymentStatus(paymentStatus, pageable);
		return savedExpenses;
	}

	public Page<ExpenseData> getExpensesByPaymentMethodWithPagination(PaymentMethod paymentMethod, Pageable pageable) {
		Page<ExpenseData> savedExpenses = elasticSearchRepository.findByPaymentMethod(paymentMethod, pageable);
		return savedExpenses;

	}

	@Transactional
	public List<ExpenseData> processExpenses(ExpensesRequest expensesRequest) {
		return expensesRequest.getExpenseRequest().stream()
				.map(this::createAndSaveExpense)
				.collect(Collectors.toList());
	}

	@Transactional
	public ExpenseData processExpense(ExpenseRequest expenseRequest) {
		return createAndSaveExpense(expenseRequest);
	}
	@Transactional
	public void deleteExpense(Integer Id){
		safelyDeleteExpense(Id);
	}
	@Transactional
	public void deleteAllExpenseOfUser(Integer Id){
		safelyDeleteUserExpense(Id);
	}

	private void safelyDeleteExpense(Integer id) {
		ExpenseData expense = this.getExpenseById(id).orElseThrow(() -> new CustomException("Expense with ID " + id + " not found"));
		User creator = findCreator(expense.getCreatedById());
		deleteUserSplits(creator,expense, expense.getUserSplits());
	}
	private void safelyDeleteUserExpense(Integer id) {
		User creator = findCreator(id);
		creator.getExpensesIds().forEach(this::safelyDeleteExpense);
		userService.saveUser(creator);
	}

	private void deleteUserSplits(User creator, ExpenseData expense, List<ExpenseUserSplits> userSplits){
		if(userSplits != null){
			userSplits.stream().map(split-> userService.getUserById(split.getUserId())).filter(Optional::isPresent).map(Optional::get).forEach(user->user.deleteExpense(expense.getId()));
			userSplits.stream().map(split-> userService.getUserById(split.getUserId())).filter(Optional::isPresent).map(Optional::get).forEach(userService::saveUser);
		}
		creator.deleteExpense(expense.getId());
		userService.saveUser(creator);
		this.deleteExpense(expense);
	}


	private ExpenseData createAndSaveExpense(ExpenseRequest expenseRequest) {
		User creator = findCreator(expenseRequest.getCreatedById());
		ExpenseData expense = expenseRequest.getExpenseData();
		expense.setCreatedById(creator.getId());
		ExpenseData savedExpense = saveExpense(expense);
		handleUserSplits(creator,savedExpense, expenseRequest.getUserSplits());
		return savedExpense; // Assuming saveExpense handles both JPA and Elasticsearch saving
	}

	private User findCreator(Integer creatorId) {
		return userService.getUserById(creatorId)
				.orElseThrow(() -> new CustomUserNotFoundException("Creator with ID " + creatorId + " not found"));
	}

	private void handleUserSplits(User creator, ExpenseData expense, List<ExpenseUserSplits> userSplits) {
		if (userSplits == null || userSplits.isEmpty()) {
			creator.addExpense(expense);
			userService.saveUser(creator);
			return;
		};
		double totalSplitAmount = userSplits.stream()
				.mapToDouble(ExpenseUserSplits::getSplitAmount)
				.sum();
		if (!Objects.equals(totalSplitAmount, expense.getAmount())) {
			throw new CustomException("The sum of split amounts does not match the total expense amount");
		}
		Set<Integer> userIds = userSplits.stream()
				.map(ExpenseUserSplits::getUserId)
				.collect(Collectors.toSet());
		if (userIds.size() != userSplits.size()) {
			throw new CustomException("Duplicate user IDs in split amounts");
		}
		userService.validateUsersExist(userIds);
		expense.setUserSplits(userSplits);
		userSplits.stream().map(split-> userService.getUserById(split.getUserId())).filter(Optional::isPresent).map(Optional::get).forEach(user -> user.addExpense(expense));
		userSplits.stream().map(split-> userService.getUserById(split.getUserId())).filter(Optional::isPresent).map(Optional::get).forEach(userService::saveUser);

	}









}
