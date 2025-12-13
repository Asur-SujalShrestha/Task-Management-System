package com.taskmanagementsystem.task_management.Controllers;

import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.DTOs.UpdateTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import com.taskmanagementsystem.task_management.Services.Implementations.TaskService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add-task")
    public ResponseEntity<Tasks> addNewTask(@RequestBody AddTaskDTO addTaskDTO) throws BadRequestException {
        if(addTaskDTO.getTitle().isEmpty()){
            throw new BadRequestException("Invalid task title.");
        }
        if(addTaskDTO.getDueDate() == null){
            throw new BadRequestException("Invalid due date.");
        }
        if(addTaskDTO.getDueDate().isBefore(LocalDate.now())){
            throw new BadRequestException("Due date cannot be before current date.");
        }
        Tasks addedTask = taskService.addTask(addTaskDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(addedTask.getId()).toUri();
        return ResponseEntity.created(location).body(addedTask);
    }

    @GetMapping("/get-all-tasks")
    public ResponseEntity<List<Tasks>> getAllTasks(@RequestParam(name = "pageNumber", defaultValue = "1", required = false) int pageNumber,
                                                   @RequestParam(name = "pageSize", defaultValue = "1", required = false) int pageSize){
        List<Tasks> allTasks = taskService.getAllTasks(PageRequest.of(pageNumber-1, pageSize));
            return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/get-task/{taskId}")
    public ResponseEntity<Tasks> getTaskById(@PathVariable long taskId){
        Tasks task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<Tasks> updateTask(@PathVariable long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) throws BadRequestException {
        if(updateTaskDTO.getDueDate().isBefore(LocalDate.now())){
            throw new BadRequestException("Due date cannot be before current date");
        }
        Tasks task = taskService.updateTask(taskId, updateTaskDTO);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/update-task-status/{taskId}")
    public ResponseEntity<Tasks> updateTaskStatus(@PathVariable long taskId, @RequestParam(name = "status") String status) throws BadRequestException {
        Tasks task = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete-task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable long taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all-tasks")
    public ResponseEntity<Void> deleteAllTasks(){
        taskService.deleteAllTasks();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-tasks-by-status")
    public ResponseEntity<List<Tasks>> getTasksByStatus(@RequestParam(name = "status") String status) throws BadRequestException {
        List<Tasks> tasks= taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
