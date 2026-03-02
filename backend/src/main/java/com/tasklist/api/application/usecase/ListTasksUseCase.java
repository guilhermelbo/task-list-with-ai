package com.tasklist.api.application.usecase;

import com.tasklist.api.application.port.in.PageResult;
import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.repository.TaskRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListTasksUseCase {

    private final TaskRepository taskRepository;

    public PageResult<Task> execute(int page, int size) {
        List<Task> tasks = taskRepository.findAll(page, size);
        return new PageResult<>(tasks, page, size);
    }
}
