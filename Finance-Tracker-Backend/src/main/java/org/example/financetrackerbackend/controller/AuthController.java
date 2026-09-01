package org.example.financetrackerbackend.controller;

import org.example.financetrackerbackend.dto.RegisterDto;
import org.example.financetrackerbackend.dto.UserDto;
import org.example.financetrackerbackend.service.AuthService;
import org.example.financetrackerbackend.service.AuthService.LoginRequest;
import org.example.financetrackerbackend.service.AuthService.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	@GetMapping("/hello-finance-tracker")
	public String hello() {
		return "Hello Finance Tracker!";
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
		String responseString = authService.register(registerDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseString);
	}

	@GetMapping("/get-user/{username}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String username) {
		UserDto userDto = authService.getUserById(username);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		LoginResponse responseString = authService.login(loginRequest);
		return ResponseEntity.status(HttpStatus.OK).body(responseString);

	}

}
