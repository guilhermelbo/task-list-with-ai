package com.tasklist.api.presentation.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.tasklist.api.domain.Task;
import java.util.HashMap;
import java.util.Map;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TaskResourceAssembler implements RepresentationModelAssembler<Task, TaskResource> {

    @Override
    public TaskResource toModel(Task entity) {
        Map<String, Object> uiMeta = new HashMap<>();

        // Business Rule: Completed tasks cannot have their title edited.
        // In the future this can also check Security Context for permissions
        uiMeta.put("canEditTitle", !entity.isCompleted());
        uiMeta.put("canDelete", true); // Example of permission flag

        TaskResource resource = TaskResource.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .completed(entity.isCompleted())
                .uiMeta(uiMeta)
                .build();

        resource.add(linkTo(methodOn(TaskController.class).getTask(entity.getId())).withSelfRel());
        resource.add(linkTo(methodOn(TaskController.class).updateTask(entity.getId(), null)).withRel("update"));
        resource.add(linkTo(methodOn(TaskController.class).deleteTask(entity.getId())).withRel("delete"));

        return resource;
    }
}
