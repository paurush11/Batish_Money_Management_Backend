package com.example.batishMoneyManager.expenseData;

import com.example.batishMoneyManager.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<ExpenseData> getExpense() {
        return expenseService.getAllExpenses();
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> saveExpense(@RequestBody ExpenseRequest expenseRequest) throws Exception {
        try {
            return ResponseEntity.ok(expenseService.processExpense(expenseRequest));
        } catch (Exception E) {
            return ResponseEntity.badRequest().body(Map.of("error", E.getMessage()));
        }
    }

    //    @Transactional
    @PostMapping("/all/")
    public ResponseEntity<?> saveExpenses(@RequestBody ExpensesRequest expensesRequest) throws Exception {
        try {
            return ResponseEntity.ok(expenseService.processExpenses(expensesRequest));
        } catch (Exception E) {
            return ResponseEntity.badRequest().body(Map.of("error", E.getMessage()));
        }
    }

    @Transactional
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<ExpenseData> updateExpense(@RequestBody ExpenseData expense, @PathVariable Integer id) {
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
    public ResponseEntity<?> deleteExpense(@PathVariable Integer id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok().build();
        } catch (Exception E) {
            return ResponseEntity.badRequest().body(Map.of("error", E.getMessage()));
        }
    }

    @Transactional
    @DeleteMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> deleteExpensesByUserId(@PathVariable Integer userId) {
        try {
            expenseService.deleteAllExpenseOfUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception E) {
            return ResponseEntity.badRequest().body(Map.of("error", E.getMessage()));
        }
    }
}
