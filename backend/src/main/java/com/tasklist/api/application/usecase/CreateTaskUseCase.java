package com.tasklist.api.application.usecase;

import com.tasklist.api.application.port.in.CreateTaskCommand;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(CreateTaskCommand command) {
        log.info("Creating task with title: {}", command.title());
        Task task = new Task(command.title());
        task.updateDescription(command.description());
        if (command.priority() != null) {
            task.updatePriority(command.priority());
        }
        Task saved = taskRepository.save(task);
        log.info("Task created with ID: {}", saved.getId());
        return saved;
    }
}
