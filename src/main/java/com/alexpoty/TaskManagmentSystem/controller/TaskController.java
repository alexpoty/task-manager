package com.alexpoty.TaskManagmentSystem.controller;

import com.alexpoty.TaskManagmentSystem.dto.TaskDto;
import com.alexpoty.TaskManagmentSystem.model.Task;
import com.alexpoty.TaskManagmentSystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(taskDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
