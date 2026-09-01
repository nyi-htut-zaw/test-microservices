package org.example.financetrackerbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Currency currency;



}
