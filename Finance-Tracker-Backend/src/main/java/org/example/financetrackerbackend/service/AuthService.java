package org.example.financetrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.financetrackerbackend.dao.UserDao;
import org.example.financetrackerbackend.dto.Currency;
import org.example.financetrackerbackend.dto.RegisterDto;
import org.example.financetrackerbackend.dto.UserDto;
import org.example.financetrackerbackend.entity.User;
import org.example.financetrackerbackend.security.SecurityUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public record LoginRequest(String username, String password){};
    public record LoginResponse(String username, Currency currency){};

    public UserDto getUserById(String username){
        return userDao.findByUsername(username)
                .stream()
                .map(user -> new UserDto(
                        user.getId(),
                       user.getFirstName(),
                       user.getLastName(),
                       user.getUsername(),
                       user.getEmail(),
                       user.getPassword(),
                       user.getCurrency(),
                       user.getBalance(),
                       user.getSaved(),
                       user.getIncome(),
                       user.getExpense()

                ))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }


    public String register(RegisterDto registerDto){
        if(userDao.findByUsername(registerDto.getUsername()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        User user = new User(
                registerDto.getFirstName(),
                registerDto.getLastName(),
                registerDto.getUsername(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getCurrency(),
                0.0,
                0.0,
                0.0,
                0.0

        );

        userDao.save(user);
        return "Registered Successfully!";
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var auth = new UsernamePasswordAuthenticationToken(loginRequest.username() , loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(auth);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Currency currency = securityUser.getUser().getCurrency();
        String username = securityUser.getUsername();
        return new LoginResponse(username, currency);

    }
    



}
