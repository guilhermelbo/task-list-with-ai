package com.tasklist.api.application.usecase;

import com.tasklist.api.application.port.in.CreateTaskCommand;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(CreateTaskCommand command) {
        Task task = new Task(command.title());
        return taskRepository.save(task);
    }
}
