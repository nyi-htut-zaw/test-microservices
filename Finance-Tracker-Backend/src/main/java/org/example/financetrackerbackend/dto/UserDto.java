package org.example.financetrackerbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Currency currency;
    private double balance;
    private double saved;
    private double income;
    private double expense;

    public UserDto(Long id, String firstName, String lastName, String username, String email, String password, Currency currency, double balance, double saved, double income, double expense) {
        this.id = id;
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
}
