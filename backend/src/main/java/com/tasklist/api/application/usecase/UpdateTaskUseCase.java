package com.tasklist.api.application.usecase;

import com.tasklist.api.application.port.in.UpdateTaskCommand;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.exception.NotFoundException;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(UpdateTaskCommand command) {
        Task task = taskRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("Task with ID " + command.id() + " not found"));

        task.updateTitle(command.title());
        if (command.completed() && !task.isCompleted()) {
            task.complete();
        }

        return taskRepository.save(task);
    }
}
