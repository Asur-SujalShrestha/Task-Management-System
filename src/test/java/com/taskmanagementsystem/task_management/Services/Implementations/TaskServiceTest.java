package com.taskmanagementsystem.task_management.Services.Implementations;

import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.DTOs.UpdateTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import com.taskmanagementsystem.task_management.Repositories.TaskRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.config.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void addTaskSuccessfully() throws BadRequestException {

        AddTaskDTO addTaskDTO = AddTaskDTO.builder()
                .title("Test")
                .description("This is a test")
                .dueDate(LocalDate.now().plusDays(1))
                .status("PENDING")
                .build();

        Tasks tasks = Tasks.builder()
                .title(addTaskDTO.getTitle())
                .description(addTaskDTO.getDescription())
                .dueDate(addTaskDTO.getDueDate())
                .status(Enum.valueOf(Tasks.Status.class, addTaskDTO.getStatus().toUpperCase()))
                .build();

        when(taskRepository.save(any(Tasks.class))).thenReturn(tasks);

        Tasks addedTask = taskService.addTask(addTaskDTO);

        assertEquals(addedTask.getTitle(), addTaskDTO.getTitle());

        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    public void getAllTasksSuccessfully() {
        List<Tasks> dummyTask = List.of(
                new Tasks(1L, "DummyTest1", "This is the dummy test 1", Tasks.Status.PENDING, LocalDate.now(), LocalDate.now().plusDays(1)),
                new Tasks(2L, "DummyTest2", "This is the dummy test 2", Tasks.Status.COMPLETED, LocalDate.now(), LocalDate.now().plusDays(2))
        );
        Pageable pageable = PageRequest.of(1,10);
        Page<Tasks> taskPage = new PageImpl<>(dummyTask, pageable, dummyTask.size());
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        List<Tasks> tasksList = taskService.getAllTasks(pageable);

        assertEquals(tasksList.size(), dummyTask.size());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getTaskByIdSuccessfully() {
        Tasks task = new Tasks(1L, "DummyTest1", "This is the dummy test 1", Tasks.Status.PENDING, LocalDate.now(), LocalDate.now().plusDays(1));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Tasks existingTask = taskService.getTaskById(1L);

        assertEquals(existingTask.getTitle(), task.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void updateTaskSuccessfully() throws BadRequestException {
        long taskId = 1L;
        Tasks existingTask = Tasks.builder()
                .title("Test task")
                .description("This is test task")
                .createDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(2))
                .status(Tasks.Status.PENDING)
                .build();

        UpdateTaskDTO updateTaskDTO = UpdateTaskDTO.builder()
                .title("Updated Test task")
                .description("This is updated test task")
                .dueDate(LocalDate.now().plusDays(2))
                .status("PENDING")
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tasks updatedTask = taskService.updateTask(taskId, updateTaskDTO);

        assertEquals(updateTaskDTO.getTitle(), updatedTask.getTitle());
        assertEquals(Tasks.Status.valueOf(updateTaskDTO.getStatus()), updatedTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    public void updateTaskStatusSuccessfully() throws BadRequestException {
        long taskId = 1L;
        String status = "IN_PROGRESS";

        Tasks existingTask = Tasks.builder()
                .id(taskId)
                .title("Test task")
                .description("This is test task")
                .createDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(2))
                .status(Tasks.Status.PENDING)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tasks updatedTask = taskService.updateTaskStatus(taskId, status);

        assertEquals(Tasks.Status.valueOf(status), updatedTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Tasks.class));

    }

    @Test
    public void deleteTaskSuccessfully() {
        long taskId = 1L;
        Tasks existingTask = Tasks.builder()
                .id(taskId)
                .title("Test task")
                .description("This is test task")
                .createDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(2))
                .status(Tasks.Status.PENDING)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskRepository).delete(existingTask);
        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    public void deleteAllTasksSuccessfully() {
        doNothing().when(taskRepository).deleteAll();
        taskService.deleteAllTasks();
        verify(taskRepository, times(1)).deleteAll();
    }

    @Test
    public void GetTaskByStatusSuccessfully() throws BadRequestException {
        String status = "PENDING";
        List<Tasks> tasksList = List.of(
                Tasks.builder()
                        .id(1L)
                        .title("Test task 1")
                        .description("This is test task 1")
                        .createDate(LocalDate.now())
                        .dueDate(LocalDate.now().plusDays(2))
                        .status(Tasks.Status.valueOf(status))
                        .build(),
                Tasks.builder()
                        .id(2L)
                        .title("Test task 2")
                        .description("This is test task 2")
                        .createDate(LocalDate.now())
                        .dueDate(LocalDate.now().plusDays(3))
                        .status(Tasks.Status.valueOf(status))
                        .build()
        );

        when(taskRepository.findByStatus(Tasks.Status.valueOf(status))).thenReturn(tasksList);
        List<Tasks> existingTaskList = taskService.getTasksByStatus(status);

        assertEquals(tasksList.size(), existingTaskList.size());
        assertEquals(tasksList.get(0).getTitle(), existingTaskList.get(0).getTitle());

        verify(taskRepository, times(1)).findByStatus(Tasks.Status.valueOf(status));
    }

}