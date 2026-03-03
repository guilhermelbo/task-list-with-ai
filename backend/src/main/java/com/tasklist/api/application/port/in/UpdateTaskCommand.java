package com.tasklist.api.application.port.in;

import com.tasklist.api.domain.TaskPriority;

public record UpdateTaskCommand(String id, String title, boolean completed, String description, TaskPriority priority) {
}
