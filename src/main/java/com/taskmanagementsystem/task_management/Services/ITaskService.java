package com.taskmanagementsystem.task_management.Services;

import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.DTOs.UpdateTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITaskService {
    Tasks addTask(AddTaskDTO addTaskDTO) throws BadRequestException;

    List<Tasks> getAllTasks(Pageable pageable);

    Tasks getTaskById(long taskId);

    Tasks updateTask(long taskId, UpdateTaskDTO updateTaskDTO) throws BadRequestException;

    Tasks updateTaskStatus(long taskId, String status) throws BadRequestException;

    void deleteTask(long taskId);

    void deleteAllTasks();

    List<Tasks> getTasksByStatus(String status) throws BadRequestException;
}
