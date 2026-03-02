package com.tasklist.api.presentation.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskResource extends RepresentationModel<TaskResource> {
    private String id;
    private String title;
    private boolean completed;

    @JsonProperty("_ui_meta")
    private Map<String, Object> uiMeta;
}
