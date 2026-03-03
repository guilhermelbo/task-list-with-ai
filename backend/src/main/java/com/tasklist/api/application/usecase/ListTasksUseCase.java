package com.tasklist.api.application.usecase;

import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListTasksUseCase {

    private final TaskRepository taskRepository;

    public Page<Task> execute(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
}
