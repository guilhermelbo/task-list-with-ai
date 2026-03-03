package com.tasklist.api.application.port.in;

import com.tasklist.api.domain.TaskPriority;

public record CreateTaskCommand(String title, String description, TaskPriority priority) {
}
