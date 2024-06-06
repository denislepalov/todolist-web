package lepdv.todolistweb.unit.service;

import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.repository.TaskRepository;
import lepdv.todolistweb.repository.UserRepository;
import lepdv.todolistweb.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static lepdv.todolistweb.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private AdminService adminService;



    @Test
    void getAllUsers_shouldGetUserList() {
        doReturn(USER_LIST).when(userRepository).findAll(Sort.by(Sort.Direction.ASC, "username"));

        List<User> actualResult = adminService.getAllUsers();

        verify(userRepository).findAll(Sort.by(Sort.Direction.ASC, "username"));
        assertFalse(actualResult.isEmpty());
        assertEquals(USER_LIST, actualResult);
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getAllUsersByFilter_shouldGetFilteredUserList_whenFilterExist() {

        final UserFilter filter = new UserFilter(USER.getUsername(), USER.getFullName(),
                LocalDate.of(2000, Month.JANUARY, 2));
        final List<User> expectedResult = List.of(USER);
        doReturn(List.of(USER)).when(userRepository).findAllByFilter(filter);

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        verify(userRepository).findAllByFilter(filter);
        assertFalse(actualResult.isEmpty());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllUsersByFilter_shouldGetAllUserList_whenFilterNotExist() {

        final UserFilter filter = new UserFilter(null, null, null);
        doReturn(USER_LIST).when(userRepository).findAllByFilter(filter);

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        verify(userRepository).findAllByFilter(filter);
        assertFalse(actualResult.isEmpty());
        assertEquals(USER_LIST, actualResult);
    }

    @Test
    void getAllUsersByFilter_shouldGetEmptyUserList_whenFilterDoNotHaveMatches() {

        final UserFilter filter = new UserFilter("dummy", "dummy",
                LocalDate.of(9999, Month.JANUARY, 2));
        doReturn(Collections.emptyList()).when(userRepository).findAllByFilter(filter);

        List<User> actualResult = adminService.getAllUsersByFilter(filter);

        verify(userRepository).findAllByFilter(filter);
        assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getUserById_shouldGetOptionalOfUser_whenExist() {
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());

        Optional<User> actualResult = adminService.getUserById(USER.getId());

        verify(userRepository).findById(USER.getId());
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(USER, actual));
    }

    @Test
    void getUserById_shouldGetEmptyOptional_whenNotExist() {
        doReturn(Optional.empty()).when(userRepository).findById(999L);

        Optional<User> actualResult = adminService.getUserById(999L);

        verify(userRepository).findById(999L);
        assertTrue(actualResult.isEmpty());
    }

    //-------------------------------------------------------------------------------------------------

    @Test
    void getAllTaskByFilterByPageable_shouldGetFilteredTaskPage_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(0, 1);
        final List<Task> taskList = List.of(TASK);
        Page<Task> taskPage = new PageImpl<>(taskList, pageable, taskList.size());
        doReturn(taskPage).when(taskRepository).findAllByFilterByPageable(filter, pageable);

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        verify(taskRepository).findAllByFilterByPageable(filter, pageable);
        assertFalse(actualResult.isEmpty());
        assertEquals(taskPage, actualResult);
    }

    @Test
    void getAllTaskByFilterByPageable_shouldGetAllTaskPage_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);
        final Pageable pageable = PageRequest.of(0, 20);
        Page<Task> taskPage = new PageImpl<>(TASK_LIST, pageable, TASK_LIST.size());
        doReturn(taskPage).when(taskRepository).findAllByFilterByPageable(filter, pageable);

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        verify(taskRepository).findAllByFilterByPageable(filter, pageable);
        assertFalse(actualResult.isEmpty());
        assertEquals(taskPage, actualResult);
    }

    @Test
    void getAllTaskByFilterByPageable_shouldGetAllTaskPage_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));
        final Pageable pageable = PageRequest.of(20, 20);
        doReturn(Page.empty()).when(taskRepository).findAllByFilterByPageable(filter, pageable);

        Page<Task> actualResult = adminService.getAllTasksByFilterByPageable(filter, pageable);

        verify(taskRepository).findAllByFilterByPageable(filter, pageable);
        assertTrue(actualResult.isEmpty());
    }

    //--------------------------------------------------------------------------------------------------

    @Test
    void getAllTasksByFilter_shouldGetFilteredTaskList_whenFilterExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(USER.getUsername(),
                LocalDate.of(2023, Month.MAY, 11));
        final List<Task> taskList = List.of(TASK, TASK_2);
        doReturn(taskList).when(taskRepository).findAllByFilter(filter);

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        verify(taskRepository).findAllByFilter(filter);
        assertFalse(actualResult.isEmpty());
        assertEquals(taskList, actualResult);
    }

    @Test
    void getAllTasksByFilter_shouldGetAllTaskList_whenFilterNotExist() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin(null, null);
        doReturn(TASK_LIST).when(taskRepository).findAllByFilter(filter);

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        verify(taskRepository).findAllByFilter(filter);
        assertFalse(actualResult.isEmpty());
        assertEquals(TASK_LIST, actualResult);
    }

    @Test
    void getAllTasksByFilter_shouldGetEmptyTaskList_whenFilterDoNotHaveMatches() {

        final TaskFilterForAdmin filter = new TaskFilterForAdmin("dummy",
                LocalDate.of(9999, Month.MAY, 11));
        doReturn(Collections.emptyList()).when(taskRepository).findAllByFilter(filter);

        List<Task> actualResult = adminService.getAllTasksByFilter(filter);

        verify(taskRepository).findAllByFilter(filter);
        assertTrue(actualResult.isEmpty());
    }


    //--------------------------------------------------------------------------------------------------

    @Test
    void getAllTasks_shouldGetTaskList() {
        doReturn(TASK_LIST).when(taskRepository).findAll(Sort.by("user", "id"));

        List<Task> actualResult = adminService.getAllTasks();

        verify(taskRepository).findAll(Sort.by("user", "id"));
        assertFalse(actualResult.isEmpty());
        assertEquals(TASK_LIST, actualResult);
    }


    @Test
    void lockUser_shouldLockUser() {
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());

        adminService.lockUser(USER.getId());

        verify(userRepository).findById(USER.getId());
    }

    @Test
    void unlockUser_shouldUnlockUser() {
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());

        adminService.unlockUser(USER.getId());

        verify(userRepository).findById(USER.getId());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        adminService.deleteUser(USER.getId());

        verify(userRepository).deleteById(USER.getId());
    }
}