package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonView(Views.Public.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;
    private String status;
    private String author;
    private String assignee;

    @JsonIgnore
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("created DESC")
    private List<Comment> comments;

    @JsonView(Views.TaskWithComments.class)
    @JsonProperty("total_comments")
    private Integer totalComments;

    @JsonIgnore
    private LocalDateTime created;

    public Task() {
    }

    public Task(String title, String description, String status, String author, String assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.author = author;
        this.created = LocalDateTime.now();
        this.assignee = assignee;
        this.totalComments = 0;
        this.comments = new ArrayList<>();
    }

    public Task(String id, String title, String description, String status, String author, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.author = author;
        this.created = LocalDateTime.now();
        this.assignee = assignee;
        this.totalComments = 0;
        this.comments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTask(this);
        this.totalComments++;
    }
}
