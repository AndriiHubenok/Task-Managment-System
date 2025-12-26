package org.example.dto;

import org.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findAllByOrderByCreatedDesc();
    List<Task> findAllByAuthorIgnoreCaseOrderByCreatedDesc(String author);
    List<Task> findAllByAuthorIgnoreCaseAndAssigneeIgnoreCaseOrderByCreatedDesc(String author, String assignee);
    List<Task> findAllByAssigneeIgnoreCaseOrderByCreatedDesc(String assignee);
}
