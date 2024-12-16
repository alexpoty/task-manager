package com.alexpoty.TaskManagmentSystem.dto;

import com.alexpoty.TaskManagmentSystem.util.Priority;
import com.alexpoty.TaskManagmentSystem.util.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {

    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
}
