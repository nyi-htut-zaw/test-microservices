package org.example.financetrackerbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.financetrackerbackend.dto.Category;
import org.example.financetrackerbackend.dto.PeriodType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Budgets extends IdClass{
    @Enumerated(EnumType.STRING)
    private Category category;
    private double limitAmount;
    private PeriodType periodType;
    private double netSpent;
    private double totalSpent;
    private double remainingAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER ,mappedBy = "budgets" , orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public Budgets(Category category, double limitAmount,PeriodType periodType,double netSpent,double totalSpent,double remainingAmount ,LocalDate startDate, LocalDate endDate, User user) {
        this.category = category;
        this.limitAmount = limitAmount;
        this.periodType = periodType;
        this.netSpent = netSpent;
        this.totalSpent = totalSpent;
        this.remainingAmount = remainingAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }
}
