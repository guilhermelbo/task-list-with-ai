package com.tasklist.api.domain.repository;

import com.tasklist.api.domain.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(String id);

    void deleteById(String id);

    List<Task> findAll(int page, int size);
}
