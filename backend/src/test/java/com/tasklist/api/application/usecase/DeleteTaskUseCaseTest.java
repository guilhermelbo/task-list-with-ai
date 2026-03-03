package com.tasklist.api.application.usecase;

import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.TaskPriority;
import com.tasklist.api.domain.exception.NotFoundException;
import com.tasklist.api.domain.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    DeleteTaskUseCase deleteTaskUseCase;

    @Test
    void shouldDeleteTaskWhenItExists() {
        var task = new Task("id-1", "Title", false, null, TaskPriority.MEDIUM, null, null);
        when(taskRepository.findById("id-1")).thenReturn(Optional.of(task));

        deleteTaskUseCase.execute("id-1");

        verify(taskRepository).deleteById("id-1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteTaskUseCase.execute("bad-id"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("bad-id");

        verify(taskRepository, never()).deleteById(any());
    }
}
