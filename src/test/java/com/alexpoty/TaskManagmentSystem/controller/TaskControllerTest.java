package com.alexpoty.TaskManagmentSystem.controller;

import com.alexpoty.TaskManagmentSystem.dto.TaskDto;
import com.alexpoty.TaskManagmentSystem.model.Task;
import com.alexpoty.TaskManagmentSystem.service.TaskService;
import com.alexpoty.TaskManagmentSystem.util.Priority;
import com.alexpoty.TaskManagmentSystem.util.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    private Task task;

    private final List<Task> tasks = new ArrayList<>();

    private TaskDto taskDto;

    @BeforeEach
    void init() {
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
    public void TaskController_CreateTask_ReturnCreated() throws Exception {
        when(taskService.createTask(any(TaskDto.class))).thenReturn(task);

        ResultActions response = mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskDto.getTitle())))
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.description", CoreMatchers.is(taskDto.getDescription())));
    }

    @Test
    public void TaskController_GetTasks_ReturnListOfTasks() throws Exception {
        when(taskService.getTasks()).thenReturn(tasks);

        ResultActions response = mockMvc.perform(get("/task")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(tasks.size())));
    }

    @Test
    public void TaskController_UpdateTask_ReturnUpdated() throws Exception {
        when(taskService.updateTask(any(TaskDto.class),any(Long.class))).thenReturn(task);

        ResultActions response = mockMvc.perform(put("/task/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskDto.getTitle())));
    }

    @Test
    public void TaskController_GetTaskById_ReturnTask() throws Exception {
        when(taskService.getTask(any(Long.class))).thenReturn(task);

        ResultActions response = mockMvc.perform(get("/task/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskDto.getTitle())));
    }

    @Test
    public void TaskController_DeleteTask_ReturnIsOk() throws Exception {
        doNothing().when(taskService).deleteTask(any(Long.class));

        ResultActions response = mockMvc.perform(delete("/task/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
