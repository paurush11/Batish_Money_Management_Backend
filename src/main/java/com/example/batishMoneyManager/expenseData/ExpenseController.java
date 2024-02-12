package com.example.batishMoneyManager.expenseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.batishMoneyManager.User.User;
import com.example.batishMoneyManager.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@AllArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;
    private final UserService userService;
    @GetMapping("/{id}")
    public Optional<ExpenseData> getExpenseById(@PathVariable Integer id) {
        return expenseService.getExpenseById(id);
    }
    @GetMapping("/")
    public List<ExpenseData> getExpense(){
    	return expenseService.getAllExpenses();
    }
    @Transactional
    @PostMapping("/")
    public ExpenseData saveExpense(@RequestBody ExpenseRequest expenseRequest) throws Exception{
        String userId = expenseRequest.getUserId();
        System.out.println(userId);
        User user = userService.getUserById(Integer.parseInt(userId)).orElseThrow(() -> new Exception("No expense recorded as User is Missing"));
        ExpenseData  expense = expenseRequest.getExpenseData();
        expense.setUserId(Integer.parseInt(userId));
        expenseService.saveExpense(expense);
        user.addExpense(expense);
        userService.saveUser(user);
        return expense;
    }
    @Transactional
    @PostMapping("/all/")
    public List<ExpenseData> saveExpenses(@RequestBody ExpensesRequest expensesRequest ) throws Exception {
        String userId = expensesRequest.getUserId();
        User user = userService.getUserById(Integer.parseInt(userId)).orElseThrow(() -> new Exception("No expense recorded as User is Missing"));
        List<ExpenseData> expenses = expensesRequest.getExpenses();
        expenses.forEach(expenseData -> expenseData.setUserId(Integer.parseInt(userId)));
        expenseService.saveAllExpenses(expenses);
        expenses.forEach(user::addExpense);
        userService.saveUser(user);
        return expenses;
    }
    @Transactional
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<ExpenseData> updateExpense( @RequestBody ExpenseData expense, @PathVariable Integer id ){
        Optional<ExpenseData> existingExpense = expenseService.getExpenseById(id);
        if (existingExpense.isPresent()) {
            ExpenseData updatedExpense = existingExpense.get();
            updatedExpense.setAmount(expense.getAmount());
            updatedExpense.setCategory(expense.getCategory());
            updatedExpense.setName(expense.getName());
            updatedExpense.setFrequency(expense.getFrequency());
            updatedExpense.setAlertsForPriceChanges(expense.getAlertsForPriceChanges());
            updatedExpense.setAutomaticBillDetection(expense.getAutomaticBillDetection());
            updatedExpense.setDueDate(expense.getDueDate());
            updatedExpense.setNotes(expense.getNotes());
            updatedExpense.setPaymentMethod(expense.getPaymentMethod());
            updatedExpense.setPaymentStatus(expense.getPaymentStatus());
            updatedExpense.setReminders(expense.getReminders());
            ExpenseData savedExpense = expenseService.saveExpense(updatedExpense);
            return ResponseEntity.ok(savedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer id){
        Optional<ExpenseData> expense =  expenseService.getExpenseById(id);
        if(expense.isPresent()){
            ExpenseData previousExpense = expense.get();
            Optional<User> user = userService.getUserById(previousExpense.getUserId());
            if(user.isPresent()){
                User savedUser = user.get();
                savedUser.deleteExpense(String.valueOf(id));
                userService.saveUser(savedUser);
            }
            expenseService.deleteExpense(previousExpense);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @Transactional
    @DeleteMapping("/user/{userId:\\d+}")
    public ResponseEntity<Void> deleteExpensesByUserId(@PathVariable Integer userId){
        Optional<User> user = userService.getUserById(userId);
        if(user.isPresent()){
            User savedUser = user.get();
            savedUser.getExpensesIds().forEach(expenseId-> {
                         Optional<ExpenseData>expense = expenseService.getExpenseById(Integer.parseInt(expenseId));
                         if(expense.isPresent()){
                             ExpenseData savedExpense = expense.get();
                             expenseService.deleteExpense(savedExpense);
                         }
                     });
             savedUser.deleteAllExpense();
             userService.saveUser(savedUser);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
