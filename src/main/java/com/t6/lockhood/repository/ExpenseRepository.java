package com.t6.lockhood.repository;

import com.t6.lockhood.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ExpenseRepository  extends JpaRepository<Expense, Integer> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.date >= :from AND e.date < :to " +
            "AND UPPER(e.expenseType) IN ('SALARY', 'SALARIES')")
    Long getTotSalaries(@Param("from") Date from, @Param("to") Date to);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.date >= :from AND e.date < :to " +
            "AND (e.expenseType IS NULL OR UPPER(e.expenseType) NOT IN ('SALARY', 'SALARIES'))")
    Long getTotOtherExpenses(@Param("from") Date from, @Param("to") Date to);
}
