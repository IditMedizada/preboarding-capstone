package com.example.tasks.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest{
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new TaskController())
            .build();
    }

    @Test
    void getTasksReturnsSavedTasks() throws Exception {
        createSampleTasks();

        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Welcome Gong"))
            .andExpect(jsonPath("$[0].done").value(false))
            .andExpect(jsonPath("$[1].title").value("Goodbye Gong"))
            .andExpect(jsonPath("$[1].done").value(true));
    }

    @Test
    void getExistingTaskReturnTaskInfoAnd200() throws Exception {
        createSampleTasks();

        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Welcome Gong"))
            .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getMissingTaskReturn404() throws Exception {

        mockMvc.perform(get("/tasks/5"))
            .andExpect(status().isNotFound());
    }

    @Test
    void CreateTaskReturnTask201() throws Exception {
        mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
            "id": 3,
            "title": "Create Task Test",
            "done": false
        }"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.title").value("Create Task Test"))
        .andExpect(jsonPath("$.done").value(false));
    }


    @Test
    void CreateInvalidTaskReturn400() throws Exception {
        mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
            "id": 3,
            "title": "",
            "done": false
        }"""))
        .andExpect(status().isBadRequest());
    }

    @Test
    void CreateInvalidTaskReturn409() throws Exception {
        createSampleTasks();
        mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
            "id": 2,
            "title": "Check",
            "done": false
        }"""))
        .andExpect(status().isConflict());
    }


    @Test
    void DeleteTaskReturn204() throws Exception {
        createSampleTasks();
        mockMvc.perform(delete("/tasks/1"))
        .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/1"))
        .andExpect(status().isNotFound());

        mockMvc.perform(delete("/tasks/1"))
        .andExpect(status().isNotFound());
    }


    private void createSampleTasks() throws Exception{
        mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
            "id": 1,
            "title": "Welcome Gong",
            "done": false
        }"""))
        .andExpect(status().isCreated());


         mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
        {
            "id": 2,
            "title": "Goodbye Gong",
            "done": true
        }"""))
        .andExpect(status().isCreated());
    }
}