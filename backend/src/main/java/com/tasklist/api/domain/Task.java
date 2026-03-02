package com.tasklist.api.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Task {
    private String id;
    private String title;
    private boolean completed;

    public Task(String title) {
        validateTitle(title);
        this.title = title;
        this.completed = false;
    }

    public Task(String id, String title, boolean completed) {
        validateTitle(title);
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public void updateTitle(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
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
