package com.taskmanagementsystem.task_management.DTOs;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTaskDTO {
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
}
