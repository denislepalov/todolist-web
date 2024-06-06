package lepdv.todolistweb.integration.service;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.service.AdminService;
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
@WithMockUser(username = "Admin", authorities = "ADMIN")
@RequiredArgsConstructor
class AdminServiceIT /*extends IntegrationTestBase*/ {

    private final AdminService adminService;


    @Test
    void getAllUsers_shouldGetUserList() {
        List<User> actualResult = adminService.getAllUsers();

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.USER_LIST, actualResult);
    }

    //--------------------------------------------------------------------------------------------------

    @Test
    void getAllUsersByFilter_shouldGetFilteredUserList_whenFilterExist() {

        final UserFilter filter = new UserFilter(Constants.USER.getUsername(), Constants.USER.getFullName(),
                LocalDate.of(2000, Month.JANUARY, 2));
        final List<User> expectedResult = List.of(Constants.USER);

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllUsersByFilter_shouldGetAllUserList_whenFilterNotExist() {
        final UserFilter filter = new UserFilter(null, null, null);

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.USER_LIST, actualResult);
    }

    @Test
    void getAllUsersByFilter_shouldGetEmptyUserList_whenFilterDoNotHaveMatches() {

        final UserFilter filter = new UserFilter("dummy", "dummy",
                LocalDate.of(9999, Month.JANUARY, 2));

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------------

    @Test
    void getUserPageByFilter_shouldGetFilteredUserPage_whenFilterExist() {

        final UserFilter filter = new UserFilter(Constants.USER.getUsername(), Constants.USER.getFullName(),
                LocalDate.of(2000, Month.JANUARY, 2));
        final Pageable pageable = PageRequest.of(0, 2);
        final Page<User> expectedResult = new PageImpl<>(List.of(Constants.USER), pageable, 1L);

        Page<User> actualResult = adminService.getUserPageByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getUserPageByFilter_shouldGetAllUserPage_whenFilterNotExist() {

        final UserFilter filter = new UserFilter(null, null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        final Page<User> expectedResult = new PageImpl<>(Constants.USER_LIST, pageable, 3L);

        Page<User> actualResult = adminService.getUserPageByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getUserPageByFilter_shouldGetEmptyUserPage_whenFilterDoNotHaveMatches() {

        final UserFilter filter = new UserFilter("dummy", "dummy",
                LocalDate.of(9999, Month.JANUARY, 2));
        final Pageable pageable = PageRequest.of(20, 20);

        Page<User> actualResult = adminService.getUserPageByFilter(filter, pageable);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------------

    @Test
    void getUserById_shouldGetOptionalOfUser_whenExist() {
        Optional<User> actualResult = adminService.getUserById(Constants.USER.getId());

        Assertions.assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> Assertions.assertEquals(Constants.USER, actual));
    }
    @Test
    void getUserById_shouldGetEmptyOptional_whenNotExist() {
        Optional<User> actualResult = adminService.getUserById(999L);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getAllTaskByFilterByPageable_shouldGetFilteredTaskPage_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(Constants.USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(0, 1);
        final List<Task> taskList = List.of(Constants.TASK);
        Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllTaskByFilterByPageable_shouldGetAllTaskPage_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        Page<Task> expectedResult = new PageImpl<>(Constants.TASK_LIST, pageable, Constants.TASK_LIST.size());

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllTaskByFilterByPageable_shouldGetAllTaskPage_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(20, 20);

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getAllTasksByFilter_shouldGetFilteredTaskList_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(Constants.USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final List<Task> expectedResult = List.of(Constants.TASK, Constants.TASK_2);

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllTasksByFilter_shouldGetAllTaskList_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.TASK_LIST, actualResult);
    }

    @Test
    void getAllTasksByFilter_shouldGetEmptyTaskList_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getAllTasks_shouldGetTaskList() {
        List<Task> actualResult = adminService.getAllTasks();

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.TASK_LIST, actualResult);
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getTaskPageByFilter_shouldGetFilteredTaskPage_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(Constants.USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(0, 5);
        final Page<Task> expectedResult = new PageImpl<>(List.of(Constants.TASK, Constants.TASK_2), pageable, 2L);

        Page<Task> actualResult = adminService.getTaskPageByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTaskPageByFilter_shouldGetAllTaskPage_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        final Page<Task> expectedResult = new PageImpl<>(Constants.TASK_LIST, pageable, 6L);

        Page<Task> actualResult = adminService.getTaskPageByFilter(filter, pageable);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTaskPageByFilter_shouldGetEmptyTaskPage_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(20, 20);

        Page<Task> actualResult = adminService.getTaskPageByFilter(filter, pageable);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void lockUser_shouldLockUser() {
        adminService.lockUser(Constants.USER.getId());
        Optional<User> optionalUser = adminService.getUserById(Constants.USER.getId());

        optionalUser.ifPresent(user -> Assertions.assertEquals(false, user.getIsNonLocked()));
    }

    //--------------------------------------------------------------------------------------------------

    @Test
    void unlockUser_shouldUnlockUser() {
        adminService.lockUser(Constants.USER.getId());
        adminService.unlockUser(Constants.USER.getId());
        Optional<User> optionalUser = adminService.getUserById(Constants.USER.getId());

        optionalUser.ifPresent(user -> Assertions.assertEquals(true, user.getIsNonLocked()));
    }

    //--------------------------------------------------------------------------------------------------

    @Test
    void deleteUser_shouldDeleteUser() {
        adminService.deleteUser(Constants.USER.getId());
        Optional<User> deletedUser = adminService.getUserById(Constants.USER.getId());

        Assertions.assertTrue(deletedUser.isEmpty());
    }

}