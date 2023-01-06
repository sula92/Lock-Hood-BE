package com.t6.lockhood.repository;

import com.t6.lockhood.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    @Query(value = "SELECT SUM(amount) FROM income WHERE date LIKE '2023-01%'", nativeQuery = true)
    Long getTotIncome();
}
