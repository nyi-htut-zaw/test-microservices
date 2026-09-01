package org.example.financetrackerbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.financetrackerbackend.dto.Currency;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends  IdClass{
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private double balance;
    private double saved;
    public double income;
    public double expense;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER ,mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "user" , orphanRemoval = true)
    private List<Budgets> budgets = new ArrayList<>();

    public User(String firstName, String lastName, String username,
                String email, String password, Currency currency, double balance, double saved,
                double income, double expense) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.currency = currency;
        this.balance = balance;
        this.saved = saved;
        this.income = income;
        this.expense = expense;
    }
    
    public void addBudgets(Budgets budgets){
        this.budgets.add(budgets);
    }


}
