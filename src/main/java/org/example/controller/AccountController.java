package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.dto.AccountRepository;
import org.example.dto.TaskRepository;
import org.example.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/api/accounts")
    public ResponseEntity<?> createAccount(@RequestBody @Valid Account account) {
        String email = account.getEmail().toLowerCase();
        account.setEmail(email);

        if (!email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().build();
        }

        if (accountRepository.existsById(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/tasks")
    @JsonView(Views.TaskWithComments.class)
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) String author, @RequestParam(required = false) String assignee) {
        if (author != null && assignee != null) {
            return ResponseEntity.ok(taskRepository.findAllByAuthorIgnoreCaseAndAssigneeIgnoreCaseOrderByCreatedDesc(author, assignee));
        } else if (author != null) {
            return ResponseEntity.ok(taskRepository.findAllByAuthorIgnoreCaseOrderByCreatedDesc(author));
        } else if (assignee != null) {
            return ResponseEntity.ok(taskRepository.findAllByAssigneeIgnoreCaseOrderByCreatedDesc(assignee));
        }
        return ResponseEntity.ok(taskRepository.findAllByOrderByCreatedDesc());
    }

    @PostMapping("/api/tasks")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRequest taskRequest) {
        return ResponseEntity.ok(taskRepository.save(new Task(taskRequest.getTitle(),
                taskRequest.getDescription(),
                "CREATED",
                SecurityContextHolder.getContext().getAuthentication().getName().toLowerCase(),
                "none")));
    }

    record TaskAssignRequest(String assignee) {}

    @PutMapping("/api/tasks/{id}/assign")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> assignTask(@PathVariable String id, @RequestBody TaskAssignRequest body) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String assignee = body.assignee().toLowerCase();

        if(assignee.equals("none")) {
            task.setAssignee("none");
            return ResponseEntity.ok(taskRepository.save(task));
        } else if(!assignee.contains("@") || !assignee.contains(".")) {
            return ResponseEntity.badRequest().build();
        } else if (!accountRepository.existsById(assignee)) {
            return ResponseEntity.notFound().build();
        } else if(!task.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        task.setAssignee(assignee);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    record TaskStatusRequest(String status) {}

    @PutMapping("/api/tasks/{id}/status")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> updateTaskStatus(@PathVariable String id, @RequestBody TaskStatusRequest body) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String status = body.status().toUpperCase();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!status.equals("CREATED") && !status.equals("IN_PROGRESS") && !status.equals("COMPLETED")) {
            return ResponseEntity.badRequest().build();
        } else if(!task.getAuthor().equals(username) && !task.getAssignee().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        task.setStatus(status);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @GetMapping("/api/tasks/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(task.getComments());
    }

    record CommentRequest(@NotBlank String text) {}

    @PostMapping("/api/tasks/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable String id, @RequestBody @Valid CommentRequest body) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String comment = body.text();
        String author = SecurityContextHolder.getContext().getAuthentication().getName();

        task.addComment(new Comment(comment, author));
        taskRepository.save(task);

        return ResponseEntity.ok().build();
    }
}
