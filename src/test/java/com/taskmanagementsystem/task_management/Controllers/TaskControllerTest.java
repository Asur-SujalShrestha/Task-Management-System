package com.taskmanagementsystem.task_management.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.DTOs.UpdateTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import com.taskmanagementsystem.task_management.Services.Implementations.TaskService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;


    @Test
    public void shouldAddTaskSuccessfully() throws Exception {
        AddTaskDTO addTaskDTO = AddTaskDTO.builder()
                .title("Controller Test 1")
                .description("This is the Controller Test 1")
                .dueDate(LocalDate.now().plusDays(1))
                .status("PENDING")
                .build();

        Tasks task = Tasks.builder()
                .id(1L)
                .title(addTaskDTO.getTitle())
                .description(addTaskDTO.getDescription())
                .dueDate(addTaskDTO.getDueDate())
                .createDate(LocalDate.now())
                .status(Tasks.Status.PENDING)
                .build();

        when(taskService.addTask(any(AddTaskDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks/add-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(addTaskDTO.getTitle()));

        verify(taskService, times(1)).addTask(any(AddTaskDTO.class));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenTitleIsNull() throws Exception {
        AddTaskDTO addTaskDTO = AddTaskDTO.builder()
                .title("")
                .description("This is the Controller Test 1")
                .dueDate(LocalDate.now().plusDays(1))
                .status("PENDING")
                .build();

        when(taskService.addTask(any(AddTaskDTO.class))).thenThrow(new BadRequestException());

        mockMvc.perform(post("/api/tasks/add-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTaskDTO)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).addTask(any(AddTaskDTO.class));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenDueDateIsNull() throws Exception {
        AddTaskDTO addTaskDTO = AddTaskDTO.builder()
                .title("Test 1")
                .description("This is the Controller Test 1")
                .dueDate(null)
                .status("PENDING")
                .build();

        when(taskService.addTask(any(AddTaskDTO.class))).thenThrow(new BadRequestException());

        mockMvc.perform(post("/api/tasks/add-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTaskDTO)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).addTask(any(AddTaskDTO.class));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenDueDateIsBeforeToday() throws Exception {
        AddTaskDTO addTaskDTO = AddTaskDTO.builder()
                .title("Test 1")
                .description("This is the Controller Test 1")
                .dueDate(LocalDate.now().minusDays(1))
                .status("PENDING")
                .build();

        when(taskService.addTask(any(AddTaskDTO.class))).thenThrow(new BadRequestException());

        mockMvc.perform(post("/api/tasks/add-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTaskDTO)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).addTask(any(AddTaskDTO.class));
    }

    @Test
    public void shouldGetAllTasksSuccessfully() throws Exception {
        List<Tasks> tasksList  = List.of(
                Tasks.builder()
                    .id(1L)
                    .title("Task 1")
                    .description("This is task 1")
                    .dueDate(LocalDate.now().plusDays(1))
                    .createDate(LocalDate.now())
                    .status(Tasks.Status.PENDING)
                    .build(),

                Tasks.builder()
                    .id(2L)
                    .title("Task 2")
                    .description("This is task 2")
                    .dueDate(LocalDate.now().plusDays(2))
                    .createDate(LocalDate.now())
                    .status(Tasks.Status.PENDING)
                    .build()
        );

        when(taskService.getAllTasks(any(PageRequest.class))).thenReturn(tasksList);

        mockMvc.perform(get("/api/tasks/get-all-tasks")
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tasksList.size()))
                .andExpect(jsonPath("$[0].title").value(tasksList.get(0).getTitle()));

        verify(taskService, times(1)).getAllTasks(any(PageRequest.class));
    }

    @Test
    public void shouldGetTaskByIDSuccessfully() throws Exception {
        long taskId = 1L;
        Tasks task = Tasks.builder()
                .id(taskId)
                .title("Task 1")
                .description("This is task 1")
                .dueDate(LocalDate.now().plusDays(1))
                .createDate(LocalDate.now())
                .status(Tasks.Status.PENDING)
                .build();

        when(taskService.getTaskById(taskId)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/get-task/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(task.getTitle()));
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    public void shouldUpdateTaskSuccessfully() throws Exception {
        long taskId = 1L;

        UpdateTaskDTO updateTaskDTO = UpdateTaskDTO.builder()
                .title("Updated Task")
                .description("This is the updated task description")
                .dueDate(LocalDate.now().plusDays(2))
                .status("IN_PROGRESS")
                .build();

        Tasks updatedTask = Tasks.builder()
                .id(taskId)
                .title(updateTaskDTO.getTitle())
                .description(updateTaskDTO.getDescription())
                .dueDate(updateTaskDTO.getDueDate())
                .createDate(LocalDate.now())
                .status(Tasks.Status.valueOf(updateTaskDTO.getStatus()))
                .build();

        when(taskService.updateTask(eq(taskId), any(UpdateTaskDTO.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/update-task/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTaskDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(updateTaskDTO.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(updateTaskDTO.getDueDate().toString()))
                .andExpect(jsonPath("$.status").value(updateTaskDTO.getStatus()));
        verify(taskService, times(1)).updateTask(eq(taskId), any(UpdateTaskDTO.class));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenTitleIsNullWhileUpdatingTask() throws Exception {
        long taskId = 1L;
        UpdateTaskDTO updateTaskDTO = UpdateTaskDTO.builder()
                .title("")
                .description("This is the updated task description")
                .dueDate(LocalDate.now().plusDays(2))
                .status("IN_PROGRESS")
                .build();

        when(taskService.updateTask(eq(taskId), any(UpdateTaskDTO.class))).thenThrow(new BadRequestException());

        mockMvc.perform(put("/api/tasks/update-task/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO))
                ).andExpect(status().isBadRequest());

        verify(taskService, never()).updateTask(eq(taskId), any(UpdateTaskDTO.class));
    }

    @Test
    public void shouldUpdateTaskStatusSuccessfully() throws Exception {
        long taskId = 1L;
        String status = "IN_PROGRESS";

        Tasks updatedTask = Tasks.builder()
                .id(taskId)
                .title("Task 1")
                .description("This is task 1")
                .dueDate(LocalDate.now().plusDays(1))
                .createDate(LocalDate.now())
                .status(Tasks.Status.valueOf(status))
                .build();

        when(taskService.updateTaskStatus(taskId, status)).thenReturn(updatedTask);

        mockMvc.perform(patch("/api/tasks/update-task-status/{taskId}", taskId)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status));

        verify(taskService, times(1)).updateTaskStatus(taskId, status);
    }

    @Test
    public void shouldDeleteTaskSuccessfully() throws Exception {
        long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/tasks/delete-task/{taskId}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    public void shouldDeleteAllTasksSuccessfully() throws Exception {
        doNothing().when(taskService).deleteAllTasks();

        mockMvc.perform(delete("/api/tasks/delete-all-tasks"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteAllTasks();
    }

    @Test
    public void shouldGetTaskByStatusSuccessfully() throws Exception {
        String status = "PENDING";
        List<Tasks> tasksList  = List.of(
                Tasks.builder()
                        .id(1L)
                        .title("Task 1")
                        .description("This is task 1")
                        .dueDate(LocalDate.now().plusDays(1))
                        .createDate(LocalDate.now())
                        .status(Tasks.Status.PENDING)
                        .build(),

                Tasks.builder()
                        .id(2L)
                        .title("Task 2")
                        .description("This is task 2")
                        .dueDate(LocalDate.now().plusDays(2))
                        .createDate(LocalDate.now())
                        .status(Tasks.Status.PENDING)
                        .build()
        );

        when(taskService.getTasksByStatus(status)).thenReturn(tasksList);

        mockMvc.perform(
                get("/api/tasks/get-tasks-by-status")
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(tasksList.get(0).getTitle()))
                .andExpect(jsonPath("$.length()").value(tasksList.size()));

        verify(taskService, times(1)).getTasksByStatus(status);
    }
}
