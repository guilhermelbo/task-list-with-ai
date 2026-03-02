package com.tasklist.api.infrastructure.persistence;

import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository springDataTaskRepository;

    @Override
    public Task save(Task task) {
        TaskJpaEntity jpaEntity = new TaskJpaEntity(task.getId(), task.getTitle(), task.isCompleted());
        TaskJpaEntity saved = springDataTaskRepository.save(jpaEntity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Task> findById(String id) {
        return springDataTaskRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public void deleteById(String id) {
        springDataTaskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll(int page, int size) {
        Page<TaskJpaEntity> pagedResult = springDataTaskRepository.findAll(PageRequest.of(page, size));
        return pagedResult.getContent().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Task mapToDomain(TaskJpaEntity jpaEntity) {
        Task task = new Task(jpaEntity.getId(), jpaEntity.getTitle());
        if (jpaEntity.isCompleted()) {
            task.complete();
        }
        return task;
    }
}
