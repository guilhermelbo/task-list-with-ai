package com.tasklist.api.application.usecase;

import com.tasklist.api.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    public void execute(String id) {
        taskRepository.deleteById(id);
    }
}
