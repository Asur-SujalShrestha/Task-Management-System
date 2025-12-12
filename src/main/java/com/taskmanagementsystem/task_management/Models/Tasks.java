package com.taskmanagementsystem.task_management.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Size(min = 1, max = 30)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = true)
    @Size(max = 100)
    private String description;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDate createDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }
}
