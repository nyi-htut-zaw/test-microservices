package org.example.financetrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.financetrackerbackend.dao.BudgetsDao;

import org.example.financetrackerbackend.dao.TransactionDao;
import org.example.financetrackerbackend.dao.UserDao;
import org.example.financetrackerbackend.dto.BudgetsDto.*;
import org.example.financetrackerbackend.dto.PeriodType;
import org.example.financetrackerbackend.dto.Type;
import org.example.financetrackerbackend.entity.Budgets;
import org.example.financetrackerbackend.entity.Transaction;
import org.example.financetrackerbackend.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.example.financetrackerbackend.dto.PeriodType.*;

@Service
@RequiredArgsConstructor
public class BudgetsService {
    private final BudgetsDao budgetsDao;
    private final UserDao userDao;
    private final TransactionDao transactionDao;



    public List<BudgetsResponse> findBudgetsByUsername(String username){
        User user = userDao.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found!")
        );
        return budgetsDao.findByUser(user).stream()
                .map(budgets -> new BudgetsResponse(
                        budgets.getId(),
                        budgets.getCategory(),
                        budgets.getLimitAmount(),
                        budgets.getPeriodType(),
                        budgets.getNetSpent(),
                        budgets.getTotalSpent(),
                        budgets.getRemainingAmount(),
                        budgets.getStartDate(),
                        budgets.getEndDate()
                ))
                .toList();
    }
    @Transactional
    public BudgetsResponse addBudgets(BudgetsRequest request, String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        LocalDate startDate;
        LocalDate endDate;

        if (request.periodType() == CUSTOM) {
            if (request.startDate() == null || request.endDate() == null) {
                throw new RuntimeException("Start date and end date are required!");
            }
            startDate = request.startDate();
            endDate = request.endDate();
        } else {
            startDate = LocalDate.now();
            endDate = switch (request.periodType()) {
                case WEEKLY -> startDate.plusWeeks(1);
                case MONTHLY -> startDate.plusMonths(1);
                case YEARLY -> startDate.plusYears(1);
                default -> throw new RuntimeException("Invalid period type!");
            };
        }


        double totalSpent = transactionDao.findByUser(user).stream()
                .filter(t -> t.getCategory() == request.category() && t.getType() == Type.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();


        double remainingAmount = request.limitAmount() - totalSpent;
        double netSpent = (request.limitAmount() == 0) ? 0 : (totalSpent / request.limitAmount()) * 100;


        Budgets budget = new Budgets(
                request.category(),
                request.limitAmount(),
                request.periodType(),
                netSpent,
                totalSpent,
                remainingAmount,
                startDate,
                endDate,
                user
        );

        user.addBudgets(budget);
        budgetsDao.save(budget);

        return new BudgetsResponse(
                budget.getId(),
                budget.getCategory(),
                budget.getLimitAmount(),
                budget.getPeriodType(),
                budget.getNetSpent(),
                budget.getTotalSpent(),
                budget.getRemainingAmount(),
                budget.getStartDate(),
                budget.getEndDate()
        );
    }

    @Transactional
    public void deleteBudgets(Long id , String username){
        User user = userDao.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found!")
        );
        Budgets budgets = budgetsDao.findByUser(user)
                .stream()
                .filter(budget -> budget.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Budgets not found!"));
        budgets.getTransactions().clear();
        budgetsDao.save(budgets);
        budgetsDao.delete(budgets);
    }
    @Transactional
    public BudgetsResponse updateBudgets(BudgetsRequest request, String username){
        User user = userDao.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found!")
        );
        Budgets budgets = budgetsDao.findByUser(user)
                .stream()
                .filter(budget -> budget.getId().equals(request.id()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Budgets not found!"));
        budgets.setCategory(request.category());
        budgets.setLimitAmount(request.limitAmount());
        budgets.setPeriodType(request.periodType());
        budgets.setStartDate(request.startDate());
        budgets.setEndDate(request.endDate());
        budgetsDao.save(budgets);
        return new BudgetsResponse(
                budgets.getId(),
                budgets.getCategory(),
                budgets.getLimitAmount(),
                budgets.getPeriodType(),
                budgets.getNetSpent(),
                budgets.getTotalSpent(),
                budgets.getRemainingAmount(),
                budgets.getStartDate(),
                budgets.getEndDate()
        );
    }

    public List<BudgetsResponse> getBudgetsByPeriodType(PeriodType periodType, String username){
        return budgetsDao.findByUsername(username).stream()
                .filter(budgets -> budgets.periodType().equals(periodType))
                .map(budgets -> new BudgetsResponse(
                        budgets.id(),
                        budgets.category(),
                        budgets.limitAmount(),
                        budgets.periodType(),
                        budgets.totalSpent(),
                        budgets.netSpent(),
                        budgets.remainingAmount(),
                        budgets.startDate(),
                        budgets.endDate()
                ))
                .toList();
    }

    public List<BudgetsResponse> getBudgetsByCategory(String category , String username){
        return budgetsDao.findByUsername(username).stream()
                .filter(budgets -> budgets.category().equals(category))
                .map(budgets -> new BudgetsResponse(
                        budgets.id(),
                        budgets.category(),
                        budgets.limitAmount(),
                        budgets.periodType(),
                        budgets.totalSpent(),
                        budgets.netSpent(),
                        budgets.remainingAmount(),
                        budgets.startDate(),
                        budgets.endDate()
                ))
                .toList();
    }

}
