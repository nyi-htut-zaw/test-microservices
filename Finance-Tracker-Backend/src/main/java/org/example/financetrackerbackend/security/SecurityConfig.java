package org.example.financetrackerbackend.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
    return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.httpBasic( Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(c-> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedMethods(List.of("*"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowedOrigins(List.of("http://localhost:5173"));
                config.setAllowCredentials(true);

                return config;
            };
            c.configurationSource(source);
        });

        http.authorizeHttpRequests(c ->{
            c.requestMatchers("/api/auth/**").permitAll();
            c.requestMatchers("/transaction/**").permitAll();
            c.requestMatchers("/budgets/**").permitAll();
            c.anyRequest().authenticated();
        });
        return http.build();
    }
}
