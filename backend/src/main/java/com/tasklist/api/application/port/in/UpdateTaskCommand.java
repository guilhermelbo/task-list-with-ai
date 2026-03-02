package com.tasklist.api.application.port.in;

public record UpdateTaskCommand(String id, String title, boolean completed) {
}
