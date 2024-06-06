package lepdv.todolistweb.integration.controller;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.PageResponse;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.util.Flag;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static lepdv.todolistweb.util.UtilObject.Fields.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@IT
@AutoConfigureMockMvc
@WithMockUser(username = "Admin", authorities = "ADMIN")
@RequiredArgsConstructor
class AdminControllerIT /*extends IntegrationTestBase*/ {

    private final MockMvc mockMvc;


    @Test
    void authorities_shouldStatus200AndGetAdminAuthoritiesPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("admin/authorities")
                );
    }

    @Test
    void showUserPageByFilter_shouldStatus200AndGetFilteredUserPage_whenFilterExist() throws Exception {
        final PageResponse<User> expectedPageResponse =
                new PageResponse<>(List.of(Constants.USER), new PageResponse.Metadata(0, 2, 1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users")
                        .param("username", "Ivan")
                        .param("fullName", "Ivanov Ivan")
                        .param("dateOfBirth", "2000-01-02")
                        .param("page", "0")
                        .param("size", "2")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("users"),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attribute("users", IsEqual.equalTo(expectedPageResponse)),
                        MockMvcResultMatchers.view().name("admin/users")
                );
    }

    @Test
    void showUserPageByFilter_shouldStatus200AndGetAllUserPage_whenFilterNotExist() throws Exception {
        final PageResponse<User> expectedPageResponse =
                new PageResponse<>(Constants.USER_LIST, new PageResponse.Metadata(0, 20, 3L));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attributeExists("users"),
                        MockMvcResultMatchers.model().attribute("users", IsEqual.equalTo(expectedPageResponse)),
                        MockMvcResultMatchers.view().name("admin/users")
                );
    }

    @Test
    void showUserById_shouldStatus200AndGetUserPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/2"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("user"),
                        MockMvcResultMatchers.model().attribute("user", IsEqual.equalTo(Constants.USER)),
                        MockMvcResultMatchers.model().attributeExists("utilObject"),
                        MockMvcResultMatchers.view().name("admin/user_by_id")
                );
    }

    @Test
    void lockUser_shouldStatus302AndRedirectToAllUsersPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/lock-user/2"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrlPattern("/admin/users/*")
                );
    }

    @Test
    void unlockUser_shouldStatus302AndRedirectToAllUsersPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/unlock-user/2"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrlPattern("/admin/users/*")
                );
    }

    @Test
    void deleteUser_shouldGetDeleteUserPage_whenFlagOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/delete-user")
                        .param(id, "2")
                        .param(flag, Flag.ONE.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("admin/delete_user")
                );
    }
    @Test
    void deleteUser_shouldGetDeleteUserPage_whenFlagTwoAndPasswordIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/delete-user")
                        .param(id, "2")
                        .param(flag, Flag.TWO.name())
                        .param(password, "Admin")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("admin/delete_user")
                );
    }
    @Test
    void deleteUser_shouldGetDeleteUserPage_whenFlagTwoAndPasswordIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/delete-user")
                        .param(id, "2")
                        .param(flag, Flag.TWO.name())
                        .param(password, "dummy")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("admin/delete_user")
                );
    }
    @Test
    void deleteUser_shouldStatus302AndRedirectToUserPage_whenFlagThree() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/delete-user")
                        .param(id, "2")
                        .param(flag, Flag.THREE.name())
                        .param(password, "Admin")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/admin/users")
                );
    }

    @Test
    void showTaskPageByFilter_shouldStatus200AndGetFilteredTaskPage_whenFilterExist() throws Exception {
        final PageResponse<Task> expectedPageResponse =
                new PageResponse<>(List.of(Constants.TASK, Constants.TASK_2, Constants.TASK_3), new PageResponse.Metadata(0, 5, 3L));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks")
                        .param("user", "Ivan")
                        .param("dateOfCreation", "2023-05-21")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attributeExists("tasks"),
                        MockMvcResultMatchers.model().attribute("tasks", IsEqual.equalTo(expectedPageResponse)),
                        MockMvcResultMatchers.view().name("admin/tasks")
                );
    }

    @Test
    void showTaskPageByFilter_shouldStatus200AndGetAllTaskPage_whenFilterNotExist() throws Exception {
        final PageResponse<Task> expectedPageResponse =
                new PageResponse<>(Constants.TASK_LIST, new PageResponse.Metadata(0, 20, 6L));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attributeExists("tasks"),
                        MockMvcResultMatchers.model().attribute("tasks", IsEqual.equalTo(expectedPageResponse)),
                        MockMvcResultMatchers.view().name("admin/tasks")
                );
    }

    @Test
    void showTaskById_shouldStatus200AndGetTaskPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks/1"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("task"),
                        MockMvcResultMatchers.model().attribute("task", IsEqual.equalTo(Constants.TASK)),
                        MockMvcResultMatchers.view().name("admin/task_by_id")
                );
    }
}