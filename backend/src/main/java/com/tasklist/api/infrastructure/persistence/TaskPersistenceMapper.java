package com.tasklist.api.infrastructure.persistence;

import com.tasklist.api.domain.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskPersistenceMapper {

    TaskJpaEntity toJpaEntity(Task task);

    Task toDomain(TaskJpaEntity entity);
}
