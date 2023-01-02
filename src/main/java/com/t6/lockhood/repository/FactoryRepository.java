package com.t6.lockhood.repository;

import com.t6.lockhood.model.Employee;
import com.t6.lockhood.model.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Integer> {

    @Query(value = "SELECT * FROM `employee` WHERE `factory_id`= :fid", nativeQuery = true)
    List<Employee> getAllFactoryPerformedEmployeeCounts(@Param("fid") int fid);
}
