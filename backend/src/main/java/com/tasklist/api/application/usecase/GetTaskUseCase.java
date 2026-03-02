package com.tasklist.api.application.usecase;

import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.exception.NotFoundException;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with ID " + id + " not found"));
    }
}
