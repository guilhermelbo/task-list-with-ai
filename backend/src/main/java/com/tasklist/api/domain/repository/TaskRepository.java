package com.tasklist.api.domain.repository;

import com.tasklist.api.domain.Task;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(String id);

    void deleteById(String id);

    Page<Task> findAll(Pageable pageable);
}
