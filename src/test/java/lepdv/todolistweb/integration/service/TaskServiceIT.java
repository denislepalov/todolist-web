package lepdv.todolistweb.integration.service;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@IT
@RequiredArgsConstructor
public class TaskServiceIT /*extends IntegrationTestBase*/ {

    private final TaskService taskService;


    @Test
    void getById_shouldGetOptionalOfTask_whenExist() {
        Optional<Task> actualResult = taskService.getById(Constants.TASK.getId());

        Assertions.assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> Assertions.assertEquals(Constants.TASK, actual));
    }

    @Test
    void getById_shouldGetEmptyOptional_whenNotExist() {
        Optional<Task> actualResult = taskService.getById(999L);

        Assertions.assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAllByUserId_shouldGetListOfTasks_whenExist() {
        List<Task> actualResult = taskService.getAllByUserId(Constants.USER.getId());
        List<String> descriptionList = actualResult.stream().map(Task::getDescription).toList();

        Assertions.assertFalse(actualResult.isEmpty());
        assertThat(actualResult).hasSize(3);
        assertThat(descriptionList).contains("Ivan task1", "Ivan task2", "Ivan task3");
    }

    @Test
    void getAllByUserId_shouldGetEmptyList_whenNotExist() {
        List<Task> actualResult = taskService.getAllByUserId(999L);

        Assertions.assertTrue(actualResult.isEmpty());
    }


    //-------------------------------------------------------------------------------------------------


    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void getAllByAuthUserByFilter_shouldGetFilteredTaskList_whenFilterExist() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(2025, Month.MAY, 12), "Not completed");
        final List<Task> expectedResult = List.of(Constants.TASK);

        List<Task> actualResult = taskService.getAllByAuthUserByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    public void getAllByAuthUserByFilter_shouldGetAllTaskList_whenFilterNotExist() {

        final TaskFilterForAuthUser filter = new TaskFilterForAuthUser(null, null);
        final List<Task> expectedResult = List.of(Constants.TASK, Constants.TASK_2, Constants.TASK_3);

        List<Task> actualResult = taskService.getAllByAuthUserByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void getAllByAuthUserByFilter_shouldGetEmptyTaskList_whenFilterDoNotHaveMatches() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(1111, Month.MAY, 11), "Completed");

        List<Task> actualResult = taskService.getAllByAuthUserByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }
    //---------------------------------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void getTaskPageByAuthUserByFilter_shouldGetFilteredTaskPage_whenFilterExist() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(2025, Month.MAY, 12), "Not completed");
        final Pageable pageable = PageRequest.of(0, 5);
        final Page<Task> expectedResult = new PageImpl<>(List.of(Constants.TASK), pageable, 1L);

        Page<Task> actualResult = taskService.getTaskPageByAuthUserByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void getTaskPageByAuthUserByFilter_shouldGetAllTaskPage_whenFilterNotExist() {

        final TaskFilterForAuthUser filter = new TaskFilterForAuthUser(null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        final Page<Task> expectedResult = new PageImpl<>(List.of(Constants.TASK, Constants.TASK_2, Constants.TASK_3), pageable, 3L);

        Page<Task> actualResult = taskService.getTaskPageByAuthUserByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void getTaskPageByAuthUserByFilter_shouldGetEmptyTaskPage_whenFilterDoNotHaveMatches() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(1111, Month.MAY, 11), "Completed");
        final Pageable pageable = PageRequest.of(20, 20);

        Page<Task> actualResult = taskService.getTaskPageByAuthUserByFilter(filter, pageable);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //---------------------------------------------------------------------------------------------------

    @Test
    void create_shouldCreateTask() {
        Task savingTask = Task.builder()
                .description("test description")
                .dueDate(LocalDate.of(2025, Month.MAY, 22))
                .build();

        taskService.create(savingTask, Constants.USER.getId());
        Optional<Task> newTask = taskService.getById(7L);

        Assertions.assertTrue(newTask.isPresent());
        newTask.ifPresent(task -> Assertions.assertAll(
                () -> Assertions.assertEquals(savingTask.getDescription(), task.getDescription()),
                () -> Assertions.assertEquals(savingTask.getDueDate(), task.getDueDate())
        ));
    }

    @Test
    void update_shouldUpdateTask() {
        final Task updatingTask = Task.builder()
                .id(1L)
                .description("Test test")
                .dateOfCreation(LocalDate.of(2023, Month.MAY, 1))
                .dueDate(LocalDate.of(2025, Month.NOVEMBER, 20))
                .isCompleted("Not completed")
                .user(Constants.USER)
                .build();

        taskService.update(updatingTask, updatingTask.getId());
        Optional<Task> updatedTask = taskService.getById(updatingTask.getId());

        updatedTask.ifPresent(task -> Assertions.assertEquals(updatingTask, task));
    }


    @Test
    void markAsCompleted_shouldMarkTaskAsCompleted() {
        taskService.markAsCompleted(Constants.TASK.getId());
        Optional<Task> markedTask = taskService.getById(Constants.TASK.getId());

        markedTask.ifPresent(task -> Assertions.assertEquals("Completed", task.getIsCompleted()));
    }

    @Test
    void delete_shouldDeleteTask() {
        taskService.delete(Constants.TASK.getId());
        Optional<Task> deletedTask = taskService.getById(Constants.TASK.getId());

        Assertions.assertTrue(deletedTask.isEmpty());
    }
}
