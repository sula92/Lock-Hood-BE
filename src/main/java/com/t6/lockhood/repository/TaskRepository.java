package com.t6.lockhood.repository;

import com.t6.lockhood.dto.TaskPriorityCountsDTO;
import com.t6.lockhood.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT COUNT(id) FROM `task` WHERE `priority`= :pri", nativeQuery = true)
    Integer getTaskPriorityCounts(@Param("pri") String pri);

    @Query(value = "SELECT COUNT(id) FROM `task` WHERE `completion`= :com", nativeQuery = true)
    Integer getCompletedCount(@Param("com") String com);
}
