package com.tasklist.api.application.usecase;

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
@Transactional(readOnly = true)
public class GetTaskUseCase {

    private final TaskRepository taskRepository;

    public Task execute(String id) {
        log.info("Fetching task with ID: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with ID " + id + " not found"));
    }
}
