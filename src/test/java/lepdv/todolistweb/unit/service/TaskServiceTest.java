package lepdv.todolistweb.unit.service;


import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.repository.TaskRepository;
import lepdv.todolistweb.repository.UserRepository;
import lepdv.todolistweb.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static lepdv.todolistweb.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskService taskService;


    @Test
    void getById_shouldGetOptionalOfTask_whenExist() {
        doReturn(Optional.of(TASK)).when(taskRepository).findById(TASK.getId());

        Optional<Task> actualResult = taskService.getById(TASK.getId());

        assertTrue(actualResult.isPresent());
        verify(taskRepository).findById(TASK.getId());
        actualResult.ifPresent(actual -> assertEquals(TASK, actual));
    }

    @Test
    void getById_shouldGetEmptyOptional_whenNotExist() {
        Optional<Task> actualResult = taskService.getById(999L);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAllByUserId_shouldGetListOfTasks_whenExist() {
        doReturn(TASK_LIST).when(taskRepository).findAllByUserIdOrderById(USER.getId());

        List<Task> actualResult = taskService.getAllByUserId(USER.getId());

        verify(taskRepository).findAllByUserIdOrderById(USER.getId());
        assertEquals(TASK_LIST, actualResult);
    }

    @Test
    void getAllByUserId_shouldGetEmptyList_whenNotExist() {
        doReturn(Collections.emptyList()).when(taskRepository).findAllByUserIdOrderById(999L);

        List<Task> actualResult = taskService.getAllByUserId(999L);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void create_shouldCreateTask() {
        final Task updatingTask = Task.builder()
                .description("Ivan task1")
                .dueDate(LocalDate.of(2025, Month.MAY, 11))
                .build();
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());
        doReturn(TASK).when(taskRepository).save(updatingTask);

        taskService.create(updatingTask, USER.getId());

        verify(userRepository).findById(USER.getId());
        verify(taskRepository).save(updatingTask);
    }

    @Test
    void update_shouldUpdateTask() {
        doReturn(Optional.of(TASK)).when(taskRepository).findById(TASK.getId());

        taskService.update(TASK, TASK.getId());

        verify(taskRepository).findById(TASK.getId());
    }


    @Test
    void markAsCompleted_shouldMarkAsCompleted() {
        final Task updatingTask = Task.builder()
                .id(1L)
                .description("Ivan task1")
                .dateOfCreation(LocalDate.of(2023, Month.MAY, 1))
                .dueDate(LocalDate.of(2025, Month.MAY, 11))
                .isCompleted("Not completed")
                .user(USER)
                .build();
        doReturn(Optional.of(updatingTask)).when(taskRepository).findById(updatingTask.getId());

        taskService.markAsCompleted(updatingTask.getId());

        verify(taskRepository).findById(updatingTask.getId());
    }

    @Test
    void delete_shouldDeleteTask() {
        doNothing().when(taskRepository).deleteById(TASK.getId());

        taskService.delete(TASK.getId());

        verify(taskRepository).deleteById(TASK.getId());
    }
}