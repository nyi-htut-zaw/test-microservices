package org.example.financetrackerbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.financetrackerbackend.dto.Category;
import org.example.financetrackerbackend.dto.Type;

import java.time.LocalDate;



@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends IdClass{

    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double amount;
    private String description;
    private LocalDate date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Budgets budgets;


}
