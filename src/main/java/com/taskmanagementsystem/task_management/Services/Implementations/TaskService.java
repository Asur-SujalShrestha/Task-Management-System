package com.taskmanagementsystem.task_management.Services.Implementations;

import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.DTOs.UpdateTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import com.taskmanagementsystem.task_management.Repositories.TaskRepository;
import com.taskmanagementsystem.task_management.Services.ITaskService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void checkStatus(String status) throws BadRequestException {
        try{
            Tasks.Status checkingStatus = Tasks.Status.valueOf(status.toUpperCase());
        }
        catch(Exception e){
            throw new BadRequestException("Invalid Status");
        }
    }

    @Override
    @Transactional
    public Tasks addTask(AddTaskDTO addTaskDTO) throws BadRequestException {
        checkStatus(addTaskDTO.getStatus());

        Tasks task = Tasks.builder()
                .title(addTaskDTO.getTitle())
                .description(addTaskDTO.getDescription())
                .status(Enum.valueOf(Tasks.Status.class, addTaskDTO.getStatus().toUpperCase()))
                .createDate(LocalDate.now())
                .dueDate(addTaskDTO.getDueDate())
                .build();

        return taskRepository.save(task);
    }

    @Override
    public List<Tasks> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).getContent();
    }

    @Override
    public Tasks getTaskById(long taskId) {
        return taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task id not found"));
    }

    @Override
    @Transactional
    public Tasks updateTask(long taskId, UpdateTaskDTO updateTaskDTO) throws BadRequestException {
        Tasks task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task id not found"));
        checkStatus(updateTaskDTO.getStatus());

        task.setTitle(updateTaskDTO.getTitle());
        task.setDescription(updateTaskDTO.getDescription());
        task.setDueDate(updateTaskDTO.getDueDate());
        task.setStatus(Enum.valueOf(Tasks.Status.class, updateTaskDTO.getStatus().toUpperCase()));
        return taskRepository.save(task);
    }

    @Override
    public Tasks updateTaskStatus(long taskId, String status) throws BadRequestException {
        Tasks task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task id not found"));
        checkStatus(status);
        task.setStatus(Enum.valueOf(Tasks.Status.class, status));
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(long taskId) {
        Tasks task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task id not found"));
        try{
            taskRepository.delete(task);
        }
        catch (Exception e){
            throw new RuntimeException("Task cannot be deleted");
        }

    }

    @Override
    public void deleteAllTasks() {
        try {
            taskRepository.deleteAll();
        }
        catch (Exception e){
            throw new RuntimeException("Task cannot be deleted");
        }
    }

    @Override
    public List<Tasks> getTasksByStatus(String status) throws BadRequestException {
        checkStatus(status);
        return taskRepository.findByStatus(Enum.valueOf(Tasks.Status.class, status));
    }
}
