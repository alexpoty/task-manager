package com.alexpoty.TaskManagmentSystem.service;

import com.alexpoty.TaskManagmentSystem.dto.TaskDto;
import com.alexpoty.TaskManagmentSystem.exception.TaskNotFoundException;
import com.alexpoty.TaskManagmentSystem.model.Task;
import com.alexpoty.TaskManagmentSystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(TaskDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .dateCreated(LocalDate.now())
                .dueDate(taskDto.getDueDate())
                .build();
        return taskRepository.save(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(TaskDto taskDto, Long id) {
        if (taskRepository.existsById(id)) {
            return taskRepository.save(Task.builder()
                    .id(id)
                    .title(taskDto.getTitle())
                    .description(taskDto.getDescription())
                    .status(taskDto.getStatus())
                    .priority(taskDto.getPriority())
                    .dueDate(taskDto.getDueDate())
                    .build());
        }
        throw new TaskNotFoundException("Task not found");
    }

    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }
}
