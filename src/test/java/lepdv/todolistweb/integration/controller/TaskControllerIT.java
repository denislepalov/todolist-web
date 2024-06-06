package lepdv.todolistweb.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.PageResponse;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.integration.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static lepdv.todolistweb.entity.Task.Fields.*;


@IT
@AutoConfigureMockMvc
@WithMockUser(username = "Ivan", authorities = "USER")
@RequiredArgsConstructor
class TaskControllerIT /*extends IntegrationTestBase*/ {

    private final MockMvc mockMvc;

    private final ObjectMapper jsonMapper = JsonMapper.builder()
            .findAndAddModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();


    @Test
    void showTaskPageByAuthUserByFilter_shouldStatus200AndGetFilteredTaskPage_whenFilterExist() throws Exception {
        final PageResponse<Task> expectedPageResponse =
                new PageResponse<>(List.of(Constants.TASK, Constants.TASK_2), new PageResponse.Metadata(0, 5, 2L));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/todo-list")
                        .param("dueDate", "2025-05-21")
                        .param("isCompleted", "Not completed")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attributeExists("todoList"),
                        MockMvcResultMatchers.model().attribute("todoList", expectedPageResponse),
                        MockMvcResultMatchers.view().name("task/todo_list")
                );
    }

    @Test
    void showTaskPageByAuthUserByFilter_shouldStatus200AndGetAllTaskPage_whenFilterNotExist() throws Exception {
        final PageResponse<Task> expectedPageResponse =
                new PageResponse<>(List.of(Constants.TASK, Constants.TASK_2, Constants.TASK_3), new PageResponse.Metadata(0, 20, 3L));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/todo-list"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("filter"),
                        MockMvcResultMatchers.model().attributeExists("todoList"),
                        MockMvcResultMatchers.model().attribute("todoList", expectedPageResponse),
                        MockMvcResultMatchers.view().name("task/todo_list")
                );
    }

    @Test
    void showById_shouldStatus200AndGetTaskPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("task"),
                        MockMvcResultMatchers.view().name("task/task_by_id")
                );
    }

    @Test
    void createTaskForm_shouldStatus200AndGetTaskCreateForm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/new"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("task"),
                        MockMvcResultMatchers.view().name("task/new")
                );
    }

    @Test
    void createTask_shouldStatus302AndRedirectToDoPage_whenDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .param(description, "new description")
                        .param(dueDate, "2025-01-01")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/tasks/todo-list")
                );
    }

    @Test
    void createTask_shouldStatus200AndGetTaskCreateForm_whenDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .param(description, "")
                        .param(dueDate, "2000-01-01")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("task/new")
                );
    }

    @Test
    void updateTaskForm_shouldStatus200AndGetTaskEditForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1/edit"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("task"),
                        MockMvcResultMatchers.view().name("task/edit")
                );
    }

    @Test
    void updateTask_shouldStatus302AndRedirectTaskPage_whenDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .param(description, "updated description")
                        .param(dateOfCreation, "2023-01-01")
                        .param(dueDate, "2025-02-02")
                        .param(isCompleted, "Not completed")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrlPattern("/tasks/*")
                );
    }

    @Test
    void updateTask_shouldStatus200AndGetTaskEditForm_whenDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .param(description, "")
                        .param(dateOfCreation, "2023-01-01")
                        .param(dueDate, "2000-02-02")
                        .param(isCompleted, "Not completed")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("task/edit")
                );
    }

    @Test
    void markAsCompleted_shouldStatus302AndRedirectTaskPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1/mark-as-completed"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrlPattern("/tasks/*")
                );
    }

    @Test
    void deleteTask_shouldStatus302AndRedirectToDoListPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/tasks/todo-list")
                );
    }


}