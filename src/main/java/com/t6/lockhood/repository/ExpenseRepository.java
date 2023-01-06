package com.t6.lockhood.repository;

import com.t6.lockhood.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository  extends JpaRepository<Expense, Integer> {

    @Query(value = "SELECT SUM(amount) FROM expense WHERE date LIKE '2023-01%'", nativeQuery = true)
    Long getTotOtherExpenses();
}
