package com.taskmanagementsystem.task_management.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Task Management System.",
    description = "This System allow user to create, view, update and delete the tasks."))
public class OpenApiConfig {
}
