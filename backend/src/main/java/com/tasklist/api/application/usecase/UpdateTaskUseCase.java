package com.tasklist.api.application.usecase;

import com.tasklist.api.application.port.in.UpdateTaskCommand;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.exception.NotFoundException;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(UpdateTaskCommand command) {
        log.info("Updating task with ID: {}", command.id());
        Task task = taskRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("Task with ID " + command.id() + " not found"));

        task.updateTitle(command.title());
        task.updateDescription(command.description());
        if (command.priority() != null) {
            task.updatePriority(command.priority());
        }
        if (command.completed()) {
            task.complete();
        }

        Task saved = taskRepository.save(task);
        log.info("Task updated with ID: {}", saved.getId());
        return saved;
    }
}
