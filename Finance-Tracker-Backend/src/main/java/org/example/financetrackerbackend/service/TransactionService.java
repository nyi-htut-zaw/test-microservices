package org.example.financetrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.financetrackerbackend.dao.TransactionDao;

import org.example.financetrackerbackend.dao.UserDao;
import org.example.financetrackerbackend.dto.TransactionDto.*;
import org.example.financetrackerbackend.dto.Type;
import org.example.financetrackerbackend.entity.Transaction;
import org.example.financetrackerbackend.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService{

     private final TransactionDao transactionDao;
     private final UserDao userDao;

     public record MonthlyTransactionSummary(
             int year,
             int month,
             double totalIncome,
             double totalExpense
     ) {}


     @Transactional
     public TransactionResponse addIncome(TransactionRequest request, String username){
          User user = userDao.findByUsername(username).orElseThrow(
                  () -> new RuntimeException("User not found!")
          );
          Transaction transaction = new Transaction();
          transaction.setCategory(request.category());
          transaction.setType(Type.INCOME);
          transaction.setAmount(request.amount());
          transaction.setDescription(request.description());
          transaction.setDate(LocalDate.now());
          double totalBalance = user.getBalance() + request.amount();
          double totalIncome = user.getIncome() + request.amount();
          user.setBalance(totalBalance);
          user.setIncome(totalIncome);
          double savedAmount = ((totalIncome)/ totalBalance)*100;
          return savedBalance(user, transaction, savedAmount);


     }

     public double getTotalIncome(String username){
          User user = userDao.findByUsername(username).orElseThrow(
                  () -> new RuntimeException("User not found!")
          );
          return user.getIncome();
     }

     @Transactional
     public TransactionResponse addExpense(TransactionRequest request, String username){
          User user = userDao.findByUsername(username).orElseThrow(
                  () -> new RuntimeException("User not found!")
          );
          Transaction transaction = new Transaction();
          transaction.setCategory(request.category());
          transaction.setType(Type.EXPENSE);
          transaction.setAmount(request.amount());
          transaction.setDescription(request.description());
          transaction.setDate(LocalDate.now());

          double totalBalance = user.getBalance() - request.amount();
          double totalExpense = user.getExpense() + request.amount();
          double getIncome = getTotalIncome(username);
          user.setBalance(totalBalance);
          user.setExpense(totalExpense);
          double savedAmount = ((totalBalance)/getIncome)*100;
          return savedBalance(user, transaction, savedAmount);
     }

     private TransactionResponse savedBalance(User user, Transaction transaction, double savedAmount) {

          user.setSaved(savedAmount);
          transaction.setUser(user);
          transactionDao.save(transaction);
          userDao.save(user);
          return new TransactionResponse(
                    transaction.getId(),
                  transaction.getType(),
                  transaction.getCategory(),
                  transaction.getAmount(),
                  transaction.getDescription(),
                  transaction.getDate()

          );
     }

     public List<TransactionResponse> listTransactionsByUsername(String username){
          return transactionDao.findTransactionByUsername(username).stream()
                  .map(transaction -> new TransactionResponse(
                          transaction.id(),
                          transaction.type(),
                          transaction.category(),
                          transaction.amount(),
                          transaction.description(),
                          transaction.date()

                  ))
                  .collect(Collectors.toList());
     }

     public List<MonthlyTransactionSummary> getLastSixMonthsSummary(String username) {
          LocalDate endDate = LocalDate.now();
          LocalDate startDate = endDate.minusMonths(5).withDayOfMonth(1); // last 6 months inclusive

          List<Transaction> transactions = transactionDao.findTransactionByDateRangeAndUsername(username, startDate, endDate);


          Map<YearMonth, List<Transaction>> grouped = transactions.stream()
                  .collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate())));

          List<MonthlyTransactionSummary> summaries = new ArrayList<>();
          for (int i = 0; i < 6; i++) {
               YearMonth month = YearMonth.from(startDate.plusMonths(i));
               List<Transaction> monthlyTxns = grouped.getOrDefault(month, Collections.emptyList());

               double totalIncome = monthlyTxns.stream()
                       .filter(t -> t.getType() == Type.INCOME)
                       .mapToDouble(Transaction::getAmount)
                       .sum();

               double totalExpense = monthlyTxns.stream()
                       .filter(t -> t.getType() == Type.EXPENSE)
                       .mapToDouble(Transaction::getAmount)
                       .sum();

               summaries.add(new MonthlyTransactionSummary(
                       month.getYear(),
                       month.getMonthValue(),
                       totalIncome,
                       totalExpense
               ));
          }

          return summaries;
     }

     public TransactionResponse getTransactionById(Long id){
          return transactionDao.findById(id).stream()

                  .map(transaction -> new TransactionResponse(
                          transaction.getId(),
                          transaction.getType(),
                          transaction.getCategory(),
                          transaction.getAmount(),
                          transaction.getDescription(),
                          transaction.getDate()

                  ))
                  .findFirst()
                  .orElseThrow(() -> new RuntimeException("Transaction not found!"));
     }
     public List<TransactionResponse> getIncomeTransaction(String username){
         return transactionDao.findTransactionByUsername(username)
                 .stream()
                 .filter(transaction -> transaction.type().equals(Type.INCOME))
                 .map(transaction -> new TransactionResponse(
                         transaction.id(),
                         transaction.type(),
                         transaction.category(),
                         transaction.amount(),
                         transaction.description(),
                         transaction.date()

                 ))
                 .toList();

     }

     public List<TransactionResponse> getExpenseTransaction(String username){
          return transactionDao.findTransactionByUsername(username)
                  .stream()
                  .filter(transaction -> transaction.type().equals(Type.EXPENSE))
                          .map(transaction -> new TransactionResponse(
                                  transaction.id(),
                                  transaction.type(),
                                  transaction.category(),
                                  transaction.amount(),
                                  transaction.description(),
                                  transaction.date()

                          ))
                          .toList();
     }

     public List<TransactionResponse> getTransactionByCategory(String username, String category){
          return transactionDao.findTransactionByUsername(username)
                  .stream()
                  .filter(transaction -> transaction.category().equals(category))
                  .map(transaction -> new TransactionResponse(
                          transaction.id(),
                          transaction.type(),
                          transaction.category(),
                          transaction.amount(),
                          transaction.description(),
                          transaction.date()

                  ))
                  .toList();
     }

     public List<TransactionResponse> getTransactionByDescription(String keyword){
          if (keyword == null || keyword.trim().isEmpty()){
               return List.of();
          }
          return transactionDao.findTransactionByDescription(keyword.trim())
                  .stream()
                  .map(transaction -> new TransactionResponse(
                          transaction.id(),
                          transaction.type(),
                          transaction.category(),
                          transaction.amount(),
                          transaction.description(),
                          transaction.date()

                  ))
                  .toList();
     }


}
