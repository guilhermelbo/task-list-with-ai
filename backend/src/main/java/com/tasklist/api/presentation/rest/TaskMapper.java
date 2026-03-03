package com.tasklist.api.presentation.rest;

import com.tasklist.api.application.port.in.CreateTaskCommand;
import com.tasklist.api.application.port.in.UpdateTaskCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    CreateTaskCommand toCommand(CreateTaskRequest request);

    @Mapping(target = "id", source = "id")
    UpdateTaskCommand toCommand(String id, UpdateTaskRequest request);
}
