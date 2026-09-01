package org.example.financetrackerbackend.dao;


import org.example.financetrackerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
