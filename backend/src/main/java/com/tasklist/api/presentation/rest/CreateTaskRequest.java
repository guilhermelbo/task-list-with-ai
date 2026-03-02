package com.tasklist.api.presentation.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required") @Size(max = 255, message = "Title cannot exceed 255 characters") String title) {
}
