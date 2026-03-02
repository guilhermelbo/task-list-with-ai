package com.tasklist.api.infrastructure.persistence;

import com.tasklist.api.domain.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskPersistenceMapper {

    TaskJpaEntity toJpaEntity(Task task);

    default Task toDomain(TaskJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return new Task(jpaEntity.getId(), jpaEntity.getTitle(), jpaEntity.isCompleted());
    }
}
