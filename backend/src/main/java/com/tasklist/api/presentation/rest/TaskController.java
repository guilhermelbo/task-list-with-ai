package com.tasklist.api.presentation.rest;

import com.tasklist.api.application.usecase.*;
import com.tasklist.api.domain.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final TaskMapper taskMapper;
    private final TaskResourceAssembler taskResourceAssembler;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResource> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.execute(taskMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResourceAssembler.toModel(task));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskResource> updateTask(@PathVariable String id,
            @Valid @RequestBody UpdateTaskRequest request) {
        Task task = updateTaskUseCase.execute(taskMapper.toCommand(id, request));
        return ResponseEntity.ok(taskResourceAssembler.toModel(task));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskResource> getTask(@PathVariable String id) {
        Task task = getTaskUseCase.execute(id);
        return ResponseEntity.ok(taskResourceAssembler.toModel(task));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List all tasks with pagination")
    public ResponseEntity<Page<TaskResource>> listTasks(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(listTasksUseCase.execute(pageable).map(taskResourceAssembler::toModel));
    }
}
