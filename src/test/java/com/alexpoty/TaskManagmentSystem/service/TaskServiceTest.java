package com.alexpoty.TaskManagmentSystem.service;

import com.alexpoty.TaskManagmentSystem.dto.TaskDto;
import com.alexpoty.TaskManagmentSystem.exception.TaskNotFoundException;
import com.alexpoty.TaskManagmentSystem.model.Task;
import com.alexpoty.TaskManagmentSystem.repository.TaskRepository;
import com.alexpoty.TaskManagmentSystem.util.Priority;
import com.alexpoty.TaskManagmentSystem.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    private Task task;

    private final List<Task> tasks = new ArrayList<>();

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test1")
                .description("test1")
                .status(Status.NEW)
                .priority(Priority.MEDIUM)
                .dateCreated(LocalDate.now())
                .build();
        tasks.add(task);
        taskDto = TaskDto.builder()
                .title("Test1")
                .description("test1")
                .status(Status.NEW)
                .priority(Priority.MEDIUM)
                .build();
    }

    @Test
    void should_create_task() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(taskDto);
        Task expectedTask = task;

        assert createdTask.getTitle().equals(expectedTask.getTitle());
        assert createdTask.getDescription().equals(expectedTask.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void should_getTask_byId() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        Task idTask = taskService.getTask(1L);
        Task expectedTask = task;

        verify(taskRepository, times(1)).findById(any(Long.class));
        assert idTask.getTitle().equals(expectedTask.getTitle());
        assert idTask.getDescription().equals(expectedTask.getDescription());
    }

    @Test
    void should_throwIfNoTaskFound() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(1L));

        verify(taskRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void should_getListOfTasks() {
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> createdTask = taskService.getTasks();

        assert createdTask.equals(tasks);
        assert createdTask.size() == 1;
        assert createdTask.getFirst().getTitle().equals(task.getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void should_updateTask() {
        when(taskRepository.existsById(any(Long.class))).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.updateTask(taskDto, 1L);
        Task expectedTask = task;

        assert createdTask.getTitle().equals(expectedTask.getTitle());
        assert createdTask.getDescription().equals(expectedTask.getDescription());

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskRepository, times(1)).existsById(any(Long.class));
    }

    @Test
    void should_throwIfUpdateFailed() {
        when(taskRepository.existsById(any(Long.class))).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskDto, 1L));

        verify(taskRepository, times(1)).existsById(any(Long.class));
    }

    @Test
    void should_deleteTask() {
        when(taskRepository.existsById(any(Long.class))).thenReturn(false);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).existsById(any(Long.class));
        verify(taskRepository, never()).deleteById(any(Long.class));
    }
}
