package lepdv.todolistweb.integration.controller;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.integration.IT;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static lepdv.todolistweb.dto.EditUserDto.Fields.*;
import static lepdv.todolistweb.util.Flag.*;
import static lepdv.todolistweb.util.UtilObject.Fields.flag;
import static lepdv.todolistweb.util.UtilObject.Fields.password;


@IT
@AutoConfigureMockMvc
@WithMockUser(username = "Ivan", authorities = "USER")
@RequiredArgsConstructor
class UserControllerIT /*extends IntegrationTestBase*/ {

    private final MockMvc mockMvc;


    @Test
    void showUser_shouldStatus200AndGetUserPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("user"),
                        MockMvcResultMatchers.model().attribute("user", IsEqual.equalTo(Constants.USER)),
                        MockMvcResultMatchers.model().attributeExists("utilObject"),
                        MockMvcResultMatchers.view().name("user/user")
                );
    }

    @Test
    void findAvatar_shouldStatus200AndGetByteArrayOfAvatar_whenDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/2/avatar"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.content().bytes(Constants.BYTE_ARRAY)
                );
    }
    @Test
    void findAvatar_shouldStatus404NotFound_whenDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/999/avatar"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editUserForm_shouldStatus200AndGetEditUserPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("user"),
                        MockMvcResultMatchers.model().attribute("user", IsEqual.equalTo(Constants.USER)),
                        MockMvcResultMatchers.view().name("user/edit")
                );
    }

    @Test
    void update_shouldStatus302AndRedirectToUserPage_whenDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/user")
                        .file(new MockMultipartFile("image", "lock.png", "multipart/form-data", Constants.BYTE_ARRAY))
                        .param("id", "2")
                        .param(username, "newUsername")
                        .param(fullName, "newFullName")
                        .param(dateOfBirth, "1999-10-10")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/user")
                );
    }
    @Test
    void update_shouldStatus200AndGetUserEditPage_whenDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/user")
                        .file(new MockMultipartFile("image", "lock.png", "multipart/form-data", Constants.BYTE_ARRAY))
                        .param("id", "2")
                        .param(fullName, "newFullName")
                        .param(dateOfBirth, "1999-10-10")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit")
                );
    }

    @Test
    void editPassword_shouldGetChangePasswordPage_whenFlagOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, ONE.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }
    @Test
    void editPassword_shouldGetChangePasswordPage_whenFlagTwoAndPasswordIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, TWO.name())
                        .param(password, "Ivan")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }
    @Test
    void editPassword_shouldGetChangePasswordPage_whenFlagTwoAndPasswordIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, TWO.name())
                        .param(password, "dummy")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }
    @Test
    void editPassword_shouldGetChangePasswordPage_whenFlagThreeAndNewPasswordIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, THREE.name())
                        .param(password, "newIvan")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }
    @Test
    void editPassword_shouldGetChangePasswordPage_whenFlagThreeAndNewPasswordIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, THREE.name())
                        .param(password, "Iv")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }
    @Test
    void editPassword_shouldStatus200AndGetChangePasswordPage_whenFlagFour() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit-password")
                        .param("id", "2")
                        .param(flag, FOUR.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/edit_password")
                );
    }

    @Test
    void deleteAccount_shouldGetDeleteAccountPage_whenFlagOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete-account")
                .param("id", "2")
                .param(flag, ONE.name())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/delete_account")
                );
    }
    @Test
    void deleteAccount_shouldGetDeleteAccountPage_whenFlagTwoAndPasswordIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete-account")
                        .param("id", "2")
                        .param(flag, TWO.name())
                        .param(password, "Ivan")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/delete_account")
                );
    }
    @Test
    void deleteAccount_shouldGetDeleteAccountPage_whenFlagTwoAndPasswordIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete-account")
                        .param("id", "2")
                        .param(flag, TWO.name())
                        .param(password, "dummy")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("user/delete_account")
                );
    }
    @Test
    void deleteAccount_shouldStatus302RedirectToLoginPage_whenFlagThree() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete-account")
                        .param("id", "2")
                        .param(flag, THREE.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/authenticate/login")
                );
    }

}