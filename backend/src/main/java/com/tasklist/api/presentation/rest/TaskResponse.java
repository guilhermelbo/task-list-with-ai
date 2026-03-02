package com.tasklist.api.presentation.rest;

public record TaskResponse(
        String id,
        String title,
        boolean completed) {
}
