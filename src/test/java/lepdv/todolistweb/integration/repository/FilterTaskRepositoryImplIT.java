package lepdv.todolistweb.integration.repository;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.repository.FilterTaskRepositoryImpl;
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

@IT
@RequiredArgsConstructor
class FilterTaskRepositoryImplIT /*extends IntegrationTestBase*/ {

    private final FilterTaskRepositoryImpl filterTaskRepository;



    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilter_shouldGetFilteredTaskList_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(Constants.USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final List<Task> expectedResult = List.of(Constants.TASK, Constants.TASK_2);

        List<Task> actualResult = filterTaskRepository.findAllByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilter_shouldGetAllTaskList_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);

        List<Task> actualResult = filterTaskRepository.findAllByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.TASK_LIST, actualResult);
    }

    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilter_shouldGetEmptyTaskList_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));

        List<Task> actualResult = filterTaskRepository.findAllByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //----------------------------------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilterByPageable_shouldGetFilteredTaskPage_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(Constants.USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(0, 1);
        final List<Task> taskList = List.of(Constants.TASK);
        Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());

        Page<Task> actualResult = filterTaskRepository.findAllByFilterByPageable(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilterByPageable_shouldGetAllTaskPage_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        Page<Task> expectedResult = new PageImpl<>(Constants.TASK_LIST, pageable, Constants.TASK_LIST.size());

        Page<Task> actualResult = filterTaskRepository.findAllByFilterByPageable(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Admin", authorities = "ADMIN")
    void findAllByFilterByPageable_shouldGetEmptyTaskPage_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(20, 20);

        Page<Task> actualResult = filterTaskRepository.findAllByFilterByPageable(filter, pageable);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //----------------------------------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void findAllByAuthUserByFilter_shouldGetFilteredTaskList_whenFilterExist() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(2025, Month.MAY, 12), "Not completed");
        final List<Task> expectedResult = List.of(Constants.TASK);

        List<Task> actualResult = filterTaskRepository.findAllByAuthUserByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void findAllByAuthUserByFilter_shouldGetAllTaskList_whenFilterNotExist() {

        final TaskFilterForAuthUser filter = new TaskFilterForAuthUser(null, null);
        final List<Task> expectedResult = List.of(Constants.TASK, Constants.TASK_2, Constants.TASK_3);

        List<Task> actualResult = filterTaskRepository.findAllByAuthUserByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(username = "Ivan", authorities = "USER")
    void findAllByAuthUserByFilter_shouldGetEmptyTaskList_whenFilterDoNotHaveMatches() {

        final TaskFilterForAuthUser filter =
                new TaskFilterForAuthUser(LocalDate.of(1111, Month.MAY, 11), "Completed");

        List<Task> actualResult = filterTaskRepository.findAllByAuthUserByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }

}