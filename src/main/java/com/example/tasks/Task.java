package com.example.tasks;

public record Task(long id, String title, boolean done){

    public Task{
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException(
                "Title must not be blank"
            );
        }
    }
}