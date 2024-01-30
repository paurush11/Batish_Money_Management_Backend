package com.example.batishMoneyManager.expenseData;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@AllArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;
    @GetMapping("/{id}")
    public Optional<ExpenseData> getExpenseById(@PathVariable Integer id) {
        return expenseService.getExpenseById(id);
    }
    @GetMapping("/")
    public List<ExpenseData> getExpenseById(){
    	return expenseService.getAllExpenses();
    }
    @PostMapping("/")
    public ExpenseData saveExpense(@RequestBody ExpenseData expense) {
        return expenseService.saveExpense(expense);
    }
    @PostMapping("/all/")
    public List<ExpenseData> saveExpenses(@RequestBody List<ExpenseData> expense) {
        return expenseService.saveAllExpenses(expense);
    }
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

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer id){
        Optional<ExpenseData> expense =  expenseService.getExpenseById(id);
        if(expense.isPresent()){
            ExpenseData previousExpense = expense.get();
            expenseService.deleteExpense(previousExpense);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
