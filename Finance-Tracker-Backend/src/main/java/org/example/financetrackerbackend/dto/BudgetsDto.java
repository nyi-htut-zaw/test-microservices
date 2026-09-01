package org.example.financetrackerbackend.dto;



import java.time.LocalDate;



public class BudgetsDto {
    public record BudgetsRequest(
            Long id,
            Category category,
            double limitAmount,
            PeriodType periodType,
            LocalDate startDate,
            LocalDate endDate){
    }
    public record BudgetsResponse(
            Long id,
            Category category,
            double limitAmount,
            PeriodType periodType,
            double netSpent,
            double totalSpent,
            double remainingAmount,
            LocalDate startDate,
            LocalDate endDate){
    }
}
