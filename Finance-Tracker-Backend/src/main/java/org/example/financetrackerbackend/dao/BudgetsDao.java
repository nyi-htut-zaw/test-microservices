package org.example.financetrackerbackend.dao;

import org.example.financetrackerbackend.dto.BudgetsDto.*;
import org.example.financetrackerbackend.entity.Budgets;
import org.example.financetrackerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetsDao extends JpaRepository<Budgets,Long> {

    List<Budgets> findByUser(User user);
    @Query("select b from Budgets b where b.user.username =:username")
    List<BudgetsResponse> findByUsername(String username);



}
