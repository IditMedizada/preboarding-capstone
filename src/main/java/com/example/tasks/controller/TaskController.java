package com.example.tasks.controller;
import com.example.tasks.Task;
import com.example.tasks.CreateTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController{

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    @GetMapping
    public List<Task> getTasks(){
        List<Task> totalTasks = new ArrayList<>();
        for(Task task: tasks.values()){
            totalTasks.add(task);
        }
        return totalTasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id){
        Task task = tasks.get(id);
        if(task == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTask request){
        if(request.title() == null || request.title().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        Task task = new Task(
            request.id(),
            request.title(),
            request.done()
        );
        Task existingTask = tasks.putIfAbsent(task.id(), task);
        if(existingTask != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        Task removeTask = tasks.remove(id);
        if(removeTask == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}