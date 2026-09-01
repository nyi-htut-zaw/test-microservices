package org.example.financetrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.financetrackerbackend.dto.TransactionDto.*;
import org.example.financetrackerbackend.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;



    @PostMapping("/add-income/{username}")
    public ResponseEntity<TransactionResponse> addIncome(@RequestBody TransactionRequest request, @PathVariable String username){
        TransactionResponse responseString = transactionService.addIncome(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseString);
    }
    @GetMapping("/get-transaction-by-id/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id){
        TransactionResponse responseString = transactionService.getTransactionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @PostMapping("/add-expense/{username}")
    public ResponseEntity<TransactionResponse> addExpense(@RequestBody TransactionRequest request, @PathVariable String username){
        TransactionResponse responseString = transactionService.addExpense(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseString);
    }
    @GetMapping("/all-transactions/{username}")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@PathVariable String username){
        List<TransactionResponse> responseString = transactionService.listTransactionsByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }
    @GetMapping("/get-transaction-by-date/{username}")
    public ResponseEntity<List<TransactionService.MonthlyTransactionSummary>> getTransactionByDate(@PathVariable String username){
        List<TransactionService.MonthlyTransactionSummary> responseString = transactionService.getLastSixMonthsSummary(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-income-transactions/{username}")
    public ResponseEntity<List<TransactionResponse>> getIncomeTransactions(@PathVariable String username){
        List<TransactionResponse> responseString = transactionService.getIncomeTransaction(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-expense-transactions/{username}")
    public ResponseEntity<List<TransactionResponse>> getExpenseTransactions(@PathVariable  String username){
        List<TransactionResponse> responseString = transactionService.getExpenseTransaction(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-transaction-by-category/{username}/{category}")
    public ResponseEntity<List<TransactionResponse>> getTransactionByCategory(@PathVariable String username,@PathVariable String category){
        List<TransactionResponse> responseString = transactionService.getTransactionByCategory(username,category);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-transaction-by-description/{keyword}")
    public ResponseEntity<List<TransactionResponse>> getTransactionByDescription(@PathVariable String keyword){
        List<TransactionResponse> responseString = transactionService.getTransactionByDescription(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

}
