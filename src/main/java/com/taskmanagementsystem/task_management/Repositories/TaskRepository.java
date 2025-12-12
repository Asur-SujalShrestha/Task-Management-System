package com.taskmanagementsystem.task_management.Repositories;

import com.taskmanagementsystem.task_management.Models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByStatus(Tasks.Status status);
}
