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
    private final TaskPersistenceMapper taskPersistenceMapper;

    @Override
    public Task save(Task task) {
        TaskJpaEntity jpaEntity = taskPersistenceMapper.toJpaEntity(task);
        TaskJpaEntity saved = springDataTaskRepository.save(jpaEntity);
        return taskPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(String id) {
        return springDataTaskRepository.findById(id).map(taskPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(String id) {
        springDataTaskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll(int page, int size) {
        Page<TaskJpaEntity> pagedResult = springDataTaskRepository.findAll(PageRequest.of(page, size));
        return pagedResult.getContent().stream()
                .map(taskPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
