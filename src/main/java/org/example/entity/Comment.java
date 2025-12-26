package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;

    private String text;
    private String author;

    @JsonIgnore
    private LocalDateTime created;

    @JsonProperty("task_id")
    public String getTaskId() {
        return task == null ? null : task.getId();
    }

    public Comment() {
    }

    public Comment(String text, String author){
        this.text = text;
        this.author = author;
        this.created = LocalDateTime.now();
    }

    public Comment(Task task, String text, String author) {
        this.task = task;
        this.text = text;
        this.author = author;
        this.created = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
