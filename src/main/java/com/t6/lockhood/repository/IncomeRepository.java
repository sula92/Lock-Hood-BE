package com.t6.lockhood.repository;

import com.t6.lockhood.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.date >= :from AND i.date < :to")
    Long getTotIncome(@Param("from") Date from, @Param("to") Date to);
}
