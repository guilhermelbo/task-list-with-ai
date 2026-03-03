package com.tasklist.api.application.usecase;

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
public class DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    public void execute(String id) {
        log.info("Deleting task with ID: {}", id);
        taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with ID " + id + " not found"));
        taskRepository.deleteById(id);
        log.info("Task deleted with ID: {}", id);
    }
}
