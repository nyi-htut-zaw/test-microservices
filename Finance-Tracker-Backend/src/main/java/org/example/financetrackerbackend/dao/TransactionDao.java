package org.example.financetrackerbackend.dao;

import org.example.financetrackerbackend.dto.TransactionDto.TransactionResponse;
import org.example.financetrackerbackend.entity.Transaction;
import org.example.financetrackerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface TransactionDao extends JpaRepository<Transaction,Long> {
    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.user.username = :username
      AND t.date BETWEEN :startDate AND :endDate
""")
    List<Transaction> findTransactionByDateRangeAndUsername(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("select t from Transaction t where t.user.username =:username")
    List<TransactionResponse> findTransactionByUsername(String username);

    @Query("select t from Transaction t where lower(t.description) like lower(concat('%' , :keyword , '%') ) ")
    List<TransactionResponse> findTransactionByDescription(@Param("keyword") String keyword);

    List<Transaction> findByUser(User user);
    String user(User user);
}
