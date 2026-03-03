package com.tasklist.api.domain;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String title;
    private boolean completed;
    private String description;
    private TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String title) {
        validateTitle(title);
        this.title = title;
        this.completed = false;
        this.priority = TaskPriority.MEDIUM;
    }

    public void updateTitle(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updatePriority(TaskPriority newPriority) {
        this.priority = newPriority != null ? newPriority : TaskPriority.MEDIUM;
    }

    public void complete() {
        this.completed = true;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
    }
}
