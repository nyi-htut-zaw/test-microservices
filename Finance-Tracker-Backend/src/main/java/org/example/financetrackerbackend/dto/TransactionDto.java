package org.example.financetrackerbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionDto {

    public record TransactionResponse(
            Long id,
            Type type,
            Category category,
            double amount,
            String description,
            LocalDate date


    ){}
    public record TransactionRequest(

            Category category,
            double amount,
            String description,
            LocalDate date


    ){}
}
