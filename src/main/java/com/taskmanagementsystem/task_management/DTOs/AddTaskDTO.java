package com.taskmanagementsystem.task_management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskDTO {
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
}
