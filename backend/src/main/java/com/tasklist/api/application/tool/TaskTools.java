package com.tasklist.api.application.tool;

import com.tasklist.api.application.port.in.CreateTaskCommand;
import com.tasklist.api.application.port.in.UpdateTaskCommand;
import com.tasklist.api.application.usecase.CreateTaskUseCase;
import com.tasklist.api.application.usecase.DeleteTaskUseCase;
import com.tasklist.api.application.usecase.GetTaskUseCase;
import com.tasklist.api.application.usecase.ListTasksUseCase;
import com.tasklist.api.application.usecase.UpdateTaskUseCase;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.TaskPriority;
import com.tasklist.api.domain.exception.NotFoundException;
import dev.langchain4j.agent.tool.Tool;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskTools {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;

    @Tool("Creates a new task with the given title, optional description, and optional priority (LOW, MEDIUM, HIGH)")
    public String createTask(String title, String description, String priority) {
        log.info("AI tool: createTask title={}", title);
        TaskPriority taskPriority = parsePriority(priority);
        Task task = createTaskUseCase.execute(new CreateTaskCommand(title, description, taskPriority));
        return "Task created successfully: ID=%s, title='%s', priority=%s, description='%s'"
                .formatted(task.getId(), task.getTitle(), task.getPriority(), task.getDescription());
    }

    @Tool("Gets a task by its ID")
    public String getTask(String id) {
        log.info("AI tool: getTask id={}", id);
        try {
            Task task = getTaskUseCase.execute(id);
            return formatTask(task);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool("Updates an existing task. Provide the task ID and any fields to update: title, completed (true/false), description, priority (LOW, MEDIUM, HIGH)")
    public String updateTask(String id, String title, Boolean completed, String description, String priority) {
        log.info("AI tool: updateTask id={}", id);
        try {
            TaskPriority taskPriority = parsePriority(priority);
            boolean isCompleted = Boolean.TRUE.equals(completed);
            Task task = updateTaskUseCase.execute(new UpdateTaskCommand(id, title, isCompleted, description, taskPriority));
            return "Task updated successfully: " + formatTask(task);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool("Deletes a task by its ID")
    public String deleteTask(String id) {
        log.info("AI tool: deleteTask id={}", id);
        try {
            deleteTaskUseCase.execute(id);
            return "Task with ID=%s deleted successfully.".formatted(id);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool("Lists tasks with pagination. page is 0-based (default 0), size is number of items per page (default 10)")
    public String listTasks(Integer page, Integer size) {
        log.info("AI tool: listTasks page={}, size={}", page, size);
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        Page<Task> result = listTasksUseCase.execute(PageRequest.of(pageNum, pageSize));

        if (result.isEmpty()) {
            return "No tasks found.";
        }

        String tasks = result.getContent().stream()
                .map(this::formatTask)
                .collect(Collectors.joining("\n"));

        return "Tasks (page %d of %d, total %d):\n%s"
                .formatted(result.getNumber() + 1, result.getTotalPages(), result.getTotalElements(), tasks);
    }

    private String formatTask(Task task) {
        return "ID=%s | title='%s' | priority=%s | completed=%s | description='%s'"
                .formatted(task.getId(), task.getTitle(), task.getPriority(),
                        task.isCompleted(), task.getDescription());
    }

    private TaskPriority parsePriority(String priority) {
        if (priority == null || priority.isBlank()) {
            return null;
        }
        try {
            return TaskPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown priority '{}', defaulting to MEDIUM", priority);
            return TaskPriority.MEDIUM;
        }
    }
}
