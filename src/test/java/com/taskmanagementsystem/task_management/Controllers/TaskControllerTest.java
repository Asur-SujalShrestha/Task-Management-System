package com.taskmanagementsystem.task_management.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagementsystem.task_management.DTOs.AddTaskDTO;
import com.taskmanagementsystem.task_management.Models.Tasks;
import com.taskmanagementsystem.task_management.Services.Implementations.TaskService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


}
